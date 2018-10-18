package me.jameszhan.pattern.reactor.nio.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
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
    private final Executor eventLoopExecutor;
    private final Selector selector;
    private final AtomicBoolean running;

    public Reactor() throws IOException {
        this.selector = Selector.open();
        this.eventLoopExecutor = Executors.newSingleThreadExecutor();
        this.running = new AtomicBoolean(false);
    }

    public Reactor register(Channel channel) throws IOException {
        SelectionKey handle = channel.register(selector);
        LOGGER.info("{} register {} with {}(interestOps: {}).", this, channel, handle, handle.interestOps());
        return this;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            LOGGER.info("{} started.", this);
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
                int readyCount = selector.select();
                if (readyCount > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey handle = iterator.next();
                        if (!handle.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        LOGGER.debug("{} with readyOps {}", handle, handle.readyOps());
                        ((Channel)handle.attachment()).dispatch(handle);
                    }
                    selectionKeys.clear();
                }
            } catch (IOException e) {
                LOGGER.error("Unexpected IOError.", e);
                close(selector);
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
