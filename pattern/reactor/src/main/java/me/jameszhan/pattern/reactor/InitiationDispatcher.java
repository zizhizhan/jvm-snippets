package me.jameszhan.pattern.reactor;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:45
 */
public class InitiationDispatcher implements Dispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitiationDispatcher.class);

    private final List<Handler> handlers = new CopyOnWriteArrayList<>();
    private final Demultiplexer demultiplexer;

    public InitiationDispatcher(Demultiplexer demultiplexer) {
        this.demultiplexer = demultiplexer;
        this.demultiplexer.register(this);
    }

    @Override
    public Dispatcher registerHandler(Handler handler) throws IOException {
        handlers.add(handler);
        SelectionKey key = handler.getSelectableChannel().register(demultiplexer.selector, handler.interestOps());
        key.attach(handler);
        return this;
    }

    @Override
    public void removeHandler(Handler handler) {
        handlers.removeIf((h) -> h == handler);
    }

    @Override
    public void handleEvents() throws IOException {
        demultiplexer.select();
    }

    @Subscribe
    public void dispatchEvent(AcceptEvent event) {
        SelectionKey key = event.getData();
        try {
            ((Handler) key.attachment()).handle(event);
        } catch (IOException e) {
            LOGGER.error("Can't handle {} via {}.", event, key.attachment(), e);
            close(key.channel());
        }
    }

    @Subscribe
    public void dispatchEvent(ReadEvent event) {
        SelectionKey key = event.getData();
        try {
            ((Handler) key.attachment()).handle(event);
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
            ((Handler) key.attachment()).handle(event);
        } catch (IOException e) {
            LOGGER.error("Can't handle {} via {}.", event, key.attachment(), e);
            close(key.channel());
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
