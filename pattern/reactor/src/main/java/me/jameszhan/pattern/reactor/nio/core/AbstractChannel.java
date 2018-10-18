package me.jameszhan.pattern.reactor.nio.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午9:02
 */
public abstract class AbstractChannel<T> implements Channel {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractChannel.class);

    protected final Map<SelectableChannel, Session<T>> sessions = new ConcurrentHashMap<>();
    protected final SelectableChannel selectableChannel;
    protected final Executor executor;

    protected AbstractChannel(SelectableChannel selectableChannel) {
        this(selectableChannel, Runnable::run);
    }

    protected AbstractChannel(SelectableChannel selectableChannel, Executor executor) {
        this.selectableChannel = selectableChannel;
        this.executor = executor;
    }

    @Override
    public void dispatch(SelectionKey handle) {
        LOGGER.info("Dispatch {}(interestOps: {}, readyOps: {}, channel: {})", handle, handle.interestOps(),
                handle.readyOps(), handle.channel());
        try {
            if (handle.isAcceptable()) {
                accept(handle);
                return;
            }
            if (handle.isReadable()) {
                read(handle);
                return;
            }
            if (handle.isWritable()) {
                write(handle);
                return;
            }
            LOGGER.error("Can't handle key {} with readyOps {}.", handle, handle.readyOps());
        } catch (IOException e) {
            if (handle.isValid()) {
                LOGGER.error("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {}) failure.",
                        handle.interestOps(), handle.readyOps(), handle.channel(), e);
            } else {
                LOGGER.debug("Dispatch SelectionKey failure.", e);
            }
        }
    }

    protected abstract void accept(SelectionKey handle);

    protected abstract void read(SelectionKey handle);

    protected void write(SelectionKey handle) throws IOException {
        Session session = sessions.get(handle.channel());
        session.send(handle);
    }

    protected static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Ignore close error.", e);
        }
    }

}
