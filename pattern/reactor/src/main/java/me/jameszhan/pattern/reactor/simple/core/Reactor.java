package me.jameszhan.pattern.reactor.simple.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
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
            } else if (key.isReadable()) {
                read(key);
            } else if (key.isWritable()) {
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
        AcceptableChannel acceptableChannel = (AcceptableChannel) key.attachment();
        acceptableChannel.accept(key);
    }

    private void read(SelectionKey key) {
        Channel channel = (Channel) key.attachment();
        try {
            Message message = channel.read(key);
            executor.execute(() -> channel.handle(message, key));
        } catch (EOFException e) {
            SelectableChannel sc = key.channel();
            if (sc instanceof SocketChannel) {
                LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
            } else {
                LOGGER.warn("SelectionKey {} closed.", key);
            }
            close(key.channel());
        } catch (IOException e) {
            LOGGER.error("Unexpected Error onChannelReadable {}.", key, e);
            close(key.channel());
        }
    }

    private void write(SelectionKey key) throws IOException {
        Channel channel = (Channel) key.attachment();
        channel.write(key);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Ignore close error.", e);
        }
    }

}
