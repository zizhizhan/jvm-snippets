package me.jameszhan.nio.reactor;

import me.jameszhan.io.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/5
 * Time: 下午8:46
 */
public class Reactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor.class);
    private final ExecutorService reactorMain = Executors.newSingleThreadExecutor();
    private final Selector selector;
    private final AtomicBoolean running;
    private final Queue<Runnable> pendingCommands;
    private final Dispatcher dispatcher;

    public Reactor(Dispatcher dispatcher) throws IOException {
        this.dispatcher = dispatcher;
        this.selector = Selector.open();
        this.pendingCommands = new ConcurrentLinkedQueue<>();
        this.running = new AtomicBoolean();
    }

    public void start() {
        reactorMain.execute(() -> {
            try {
                LOGGER.info("Reactor started, waiting for events...");
                running.compareAndSet(false, true);
                eventLoop();
            } catch (IOException e) {
                LOGGER.error("exception in event loop", e);
            }
        });
    }

    public Reactor register(Channel channel) throws IOException {
        SelectionKey key = channel.getSelectableChannel().register(selector, channel.interestOps());
        key.attach(channel);
        if (channel instanceof AbstractChannel) {
            ((AbstractChannel)channel).setReactor(this);
        }
        return this;
    }

    private void eventLoop() throws IOException {
        while (running.get()) {
            if (Thread.interrupted()) {
                running.compareAndSet(true, false);
                break;
            }

            // Synchronous pendingCommands first
            processPendingCommands();
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", selector.selectedKeys());
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", selector.selectedKeys());
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (!key.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        processKey(key);
                    }
                    selectionKeys.clear();
            }
        }
    }

    private void processKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            onChannelAcceptable(key);
        } else if (key.isReadable()) {
            onChannelReadable(key);
        } else if (key.isWritable()) {
            onChannelWritable(key);
        }
    }

    private void onChannelAcceptable(SelectionKey key) {
        try {
            SocketChannel socketChannel = ((Channel) key.attachment()).accept(key);
            if (socketChannel != null) {
                SelectionKey readKey = socketChannel.register(selector, SelectionKey.OP_READ);
                readKey.attach(key.attachment());
            }
        } catch (IOException e) {
            LOGGER.error("Unexpected Error onChannelReadable {}.", key, e);
            IOUtils.close(key.channel());
        }
    }

    private void onChannelReadable(SelectionKey key) {
        try {
            // reads the incoming data in context of reactor main loop. Can this be improved?
            Object readObject = ((Channel) key.attachment()).read(key);
            dispatcher.onChannelReadEvent((Channel) key.attachment(), readObject, key);
        } catch (EOFException e) {
            SelectableChannel sc = key.channel();
            if (sc instanceof SocketChannel) {
                LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
            } else {
                LOGGER.warn("SelectionKey {} closed.", key);
            }
            IOUtils.close(key.channel());
        } catch (IOException e) {
            LOGGER.error("Unexpected Error onChannelReadable {}.", key, e);
            IOUtils.close(key.channel());
        }
    }

    private void onChannelWritable(SelectionKey key) throws IOException {
        Channel channel = (Channel) key.attachment();
        channel.flush(key);
    }

    private void processPendingCommands() {
        Iterator<Runnable> iterator = pendingCommands.iterator();
        while (iterator.hasNext()) {
            Runnable command = iterator.next();
            command.run();
            iterator.remove();
        }
    }

    /**
     * Queues the change of operations request of a channel, which will change the interested operations of the channel
     * sometime in future.
     *
     * This is a non-blocking method and does not guarantee that the operations have changed when this method returns.
     *
     * @param key the key for which operations have to be changed.
     * @param interestedOps the new interest operations.
     */
    public void changeOps(SelectionKey key, int interestedOps) {
        pendingCommands.add(new ChangeKeyOpsCommand(key, interestedOps));
        selector.wakeup();
    }

    /**
     * A command that changes the interested operations of the key provided.
     */
    class ChangeKeyOpsCommand implements Runnable {
        private SelectionKey key;
        private int interestedOps;

        public ChangeKeyOpsCommand(SelectionKey key, int interestedOps) {
            this.key = key;
            this.interestedOps = interestedOps;
        }

        public void run() {
            key.interestOps(interestedOps);
        }

        @Override
        public String toString() {
            return "Change of ops to: " + interestedOps;
        }
    }

}
