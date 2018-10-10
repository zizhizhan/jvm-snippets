package me.jameszhan.pattern.reactor.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:02
 */
public class Reactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor.class);

    private final Map<SelectableChannel, Queue<Message>> channelToPendingWrites = new ConcurrentHashMap<>();
    private final Selector selector;
    private final Executor executor;
    private final AtomicBoolean running;

    public Reactor(int poolSize) throws IOException {
        this.selector = Selector.open();
        this.running = new AtomicBoolean(false);
        if (poolSize > 0) {
            executor = Executors.newFixedThreadPool(poolSize);
        } else {
            executor = Runnable::run;
        }
    }

    public Reactor register(Channel channel) throws IOException {
        SelectionKey key = channel.getSelectableChannel().register(selector, channel.interestOps(), channel);
        LOGGER.debug("Register {} with key {}.", channel, key);
        return this;
    }

    public void start() throws IOException {
        eventLoop();
    }

    public void stop() {
        running.compareAndSet(true, false);
        if (executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdown();
        }
    }

    private void eventLoop() throws IOException {
        if (running.compareAndSet(false, true)) {
            while (running.get()) {
                if (Thread.interrupted()) {
                    running.compareAndSet(true, false);
                    break;
                }
                int readyOps = selector.select();
                if (readyOps > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (!key.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        LOGGER.info("{} with ops {}", key, key.readyOps());
                        dispatch(key);
                    }
                    selectionKeys.clear();
                } else {
                    LOGGER.warn("Select error for {} with readyOps {}.", selector.selectedKeys(), readyOps);
                }
            }
        }
    }

    private void dispatch(SelectionKey key) {
        LOGGER.info("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {})", key.interestOps(),
                key.readyOps(), key.channel());
        try {
            if (key.isAcceptable()) {
                accept(key);
            }
            if (key.isReadable()) {
                read(key);
            }
            if (key.isWritable()) {
                write(key);
            }
        } catch (IOException e) {
            if (key.isValid()) {
                LOGGER.error("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {}) failure.",
                        key.interestOps(), key.readyOps(), key.channel(), e);
            } else {
                LOGGER.debug("Dispatch SelectionKey failure.", e);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        SelectionKey readKey = socketChannel.register(key.selector(), SelectionKey.OP_READ, key.attachment());
        LOGGER.debug("Accept {} with {}.", socketChannel, readKey);
    }

    private void read(SelectionKey key) throws IOException {
        SelectableChannel selectableChannel = key.channel();
        Channel channel = (Channel)key.attachment();
        if (selectableChannel instanceof SocketChannel) {
            SocketChannel socketChannel = (SocketChannel) selectableChannel;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int length = socketChannel.read(buffer);
            buffer.flip();
            if (length == -1) {
                SelectableChannel sc = key.channel();
                if (sc instanceof SocketChannel) {
                    LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
                } else {
                    LOGGER.warn("SelectionKey {} closed.", key);
                }
                key.channel().close();
            } else {
                SocketAddress clientAddr = socketChannel.getRemoteAddress();
                executor.execute(() -> channel.handle(new Message(buffer, clientAddr)));
            }
        } else if (selectableChannel instanceof DatagramChannel) {
            DatagramChannel datagramChannel = (DatagramChannel) selectableChannel;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketAddress clientAddr = datagramChannel.receive(buffer);
            buffer.flip();
            executor.execute(() -> channel.handle(new Message(buffer, clientAddr)));
        } else {
            LOGGER.error("Read Unexpected SelectionKey {}.", key);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SelectableChannel selectableChannel = key.channel();
        Queue<Message> pendingWrites = this.channelToPendingWrites.get(key.channel());
        while (running.get()) {
            Message pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                key.interestOps(SelectionKey.OP_READ);
                break;
            }
            if (selectableChannel instanceof SocketChannel) {
                ((SocketChannel) selectableChannel).write(pendingWrite.buffer);
            } else if (selectableChannel instanceof DatagramChannel) {
                ((DatagramChannel) selectableChannel).send(pendingWrite.buffer, pendingWrite.clientAddr);
            } else {
                LOGGER.error("Read Unexpected SelectionKey {}.", key);
            }
        }

    }
}
