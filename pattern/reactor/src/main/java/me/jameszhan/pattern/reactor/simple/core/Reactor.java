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

    private final Selector demultiplexer;
    private final Executor executor;
    private final AtomicBoolean running;

    public Reactor(int poolSize) throws IOException {
        this.demultiplexer = Selector.open();
        this.running = new AtomicBoolean(false);
        if (poolSize > 0) {
            executor = Executors.newFixedThreadPool(poolSize);
        } else {
            executor = Runnable::run;
        }
    }

    public Reactor register(Channel channel) throws IOException {
        SelectionKey key = channel.getSelectableChannel().register(demultiplexer, channel.interestOps(), channel);
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
                int readyOps = demultiplexer.select();
                if (readyOps > 0) {
                    Set<SelectionKey> selectionKeys = demultiplexer.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey handle = iterator.next();
                        if (!handle.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        LOGGER.info("{} with ops {}", handle, handle.readyOps());
                        dispatch(handle);
                    }
                    selectionKeys.clear();
                } else {
                    LOGGER.warn("Select error for {} with readyOps {}.", demultiplexer.selectedKeys(), readyOps);
                }
            }
        }
    }

    private void dispatch(SelectionKey handle) {
        LOGGER.info("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {})", handle.interestOps(),
                handle.readyOps(), handle.channel());
        try {
            if (handle.isAcceptable()) {
                accept(handle);
            } else if (handle.isReadable()) {
                read(handle);
            } else if (handle.isWritable()) {
                write(handle);
            }
        } catch (IOException e) {
            if (handle.isValid()) {
                LOGGER.error("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {}) failure.",
                        handle.interestOps(), handle.readyOps(), handle.channel(), e);
            } else {
                LOGGER.debug("Dispatch SelectionKey failure.", e);
            }
        }
    }

    private void accept(SelectionKey handle) throws IOException {
        AcceptableChannel acceptableChannel = (AcceptableChannel) handle.attachment();
        acceptableChannel.accept(handle);
    }

    private void read(SelectionKey handle) {
        Channel channel = (Channel) handle.attachment();
        try {
            Message message = channel.read(handle);
            executor.execute(() -> channel.handle(message, handle));
        } catch (EOFException e) {
            SelectableChannel sc = handle.channel();
            if (sc instanceof SocketChannel) {
                LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
            } else {
                LOGGER.warn("SelectionKey {} closed.", handle);
            }
            close(handle.channel());
        } catch (IOException e) {
            LOGGER.error("Unexpected Error onChannelReadable {}.", handle, e);
            close(handle.channel());
        }
    }

    private void write(SelectionKey handle) throws IOException {
        Channel channel = (Channel) handle.attachment();
        channel.write(handle);
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
