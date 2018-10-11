package me.jameszhan.pattern.reactor.tcp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午8:17
 */
public class Reactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor.class);
    private final String name;
    private final ExecutorService eventLoopExecutor;
    private final Selector demultiplexer;
    private final Dispatcher dispatcher;
    private final AtomicBoolean running;

    public Reactor(String name, Dispatcher dispatcher, Selector selectorProvider) throws IOException {
        this.name = name;
        this.demultiplexer = selectorProvider;
        this.dispatcher = dispatcher;
        this.eventLoopExecutor = Executors.newSingleThreadExecutor();
        this.running = new AtomicBoolean(false);
    }

    public Reactor register(SelectableChannel channel, int interestOps, Channel attachment) throws IOException {
        SelectionKey handle = channel.register(demultiplexer, interestOps, attachment);
        LOGGER.info("{} register {} with {}(interestOps: {}).", name, channel, handle, interestOps);
        return this;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            LOGGER.info("{} started.", name);
            eventLoopExecutor.execute(this::eventLoop);
        }
    }

    private void eventLoop() {
        while (running.get()) {
            if (Thread.interrupted()) {
                running.compareAndSet(true, false);
                break;
            }
            try {
                int readyCount = demultiplexer.select();
                if (readyCount > 0) {
                    LOGGER.info("{} with readyCount {}.", name, readyCount);
                    Set<SelectionKey> selectionKeys = demultiplexer.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey handle = iterator.next();
                        if (!handle.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        LOGGER.info("{} with readOps {}", name, handle.readyOps());
                        dispatcher.dispatch(handle);
                    }
                    selectionKeys.clear();
                }
            } catch (IOException e) {
                LOGGER.error("Unexpected IOError.", e);
                close(demultiplexer);
            }
        }
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Ignore close error.", e);
        }
    }
}
