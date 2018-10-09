package me.jameszhan.pattern.reactor.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:45
 */
public class InitiationDispatcher implements Dispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitiationDispatcher.class);
    private final List<EventHandler> handlers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean running;
    private final EventBus eventBus;
    private final Selector selector;
    private final Executor executor;

    public InitiationDispatcher(int poolSize) throws IOException {
        this.selector = Selector.open();
        this.running = new AtomicBoolean();
        this.eventBus = new EventBus();
        this.eventBus.register(this);
        if (poolSize > 0) {
            this.executor = Executors.newFixedThreadPool(poolSize);
        } else {
            this.executor = Runnable::run;
        }

    }

    @Override
    public Dispatcher registerHandler(EventHandler handler) throws IOException {
        handlers.add(handler);
        SelectionKey key = handler.getSelectableChannel().register(selector, handler.interestOps(), handler);
        LOGGER.debug("Register {} with key {}.", handler, key);
        return this;
    }

    @Override
    public void removeHandler(EventHandler handler) {
        handlers.removeIf((h) -> h == handler);
    }

    @Override
    public void handleEvents() throws IOException {
        this.running.compareAndSet(false, true);
        eventLoop();
    }

    @Subscribe
    public void dispatchEvent(AcceptEvent event) {
        SelectionKey key = event.getData();
        try {
            ((EventHandler) key.attachment()).handle(event);
        } catch (IOException e) {
            LOGGER.error("Can't handle {} via {}.", event, key.attachment(), e);
            close(key.channel());
        }
    }

    @Subscribe
    public void dispatchEvent(ReadEvent event) {
        SelectionKey key = event.getData();
        EventHandler handler = (EventHandler) key.attachment();
        try {
            Object readObject = handler.handle(event);
            executor.execute(() -> {
                handler.getInboundHandler().read(readObject, key);
            });
        }  catch (EOFException e) {
            SelectableChannel sc = key.channel();
            if (sc instanceof SocketChannel) {
                LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
            } else {
                LOGGER.warn("SelectionKey {} closed.", key);
            }
            close(key.channel());
        } catch (IOException e) {
            LOGGER.error("Can't handle {} via {}.", event, key.attachment(), e);
            close(key.channel());
        }
    }

    @Subscribe
    public void dispatchEvent(WriteEvent event) {
        SelectionKey key = event.getData();
        try {
            ((EventHandler) key.attachment()).handle(event);
        } catch (IOException e) {
            LOGGER.error("Can't handle {} via {}.", event, key.attachment(), e);
            close(key.channel());
        }
    }

    private void eventLoop() throws IOException {
        while (running.get()) {
            if (Thread.interrupted()) {
                running.compareAndSet(true, false);
                break;
            }
            int op = selector.select();  // select(handlers)
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", selector.selectedKeys());
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", selector.selectedKeys());
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys(); // handles
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
            }
        }
    }

    private void dispatch(SelectionKey key) {
        if (key.isAcceptable()) {
            eventBus.post(new AcceptEvent(this, key));
        } else if (key.isReadable()) {
            eventBus.post(new ReadEvent(this, key));
        } else if (key.isWritable()) {
            eventBus.post(new WriteEvent(this, key));
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
