package me.jameszhan.pattern.reactor.tcp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午10:12
 */
public class ReadWriteDispatcher implements Dispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteDispatcher.class);

    private final Executor executor;

    public ReadWriteDispatcher(int poolSize) {
        if (poolSize > 0) {
            executor = Executors.newFixedThreadPool(poolSize);
        } else {
            executor = Runnable::run;
        }
    }

    @Override
    public void dispatch(SelectionKey handle) {
        LOGGER.info("Dispatch SelectionKey(interestOps: {}, readyOps: {}, channel: {})", handle.interestOps(),
                handle.readyOps(), handle.channel());
        try {
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

    private void read(SelectionKey handle) {
        Session channel = (Session) handle.attachment();
        try {
            ByteBuffer buffer = channel.read(handle);
            executor.execute(() -> channel.handle(buffer, handle));
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
        Session channel = (Session) handle.attachment();
        channel.send(handle);
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
