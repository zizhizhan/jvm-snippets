package me.jameszhan.pattern.reactor.nio.core;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.Executor;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午9:08
 */
public class TcpChannel extends AbstractChannel<ByteBuffer> {

    protected final SessionHandler<ByteBuffer> sessionHandler;

    public TcpChannel(int port, SessionHandler<ByteBuffer> sessionHandler) throws IOException {
        super(buildServerSocketChannel(port));
        this.sessionHandler = sessionHandler;
    }

    public TcpChannel(int port, SessionHandler<ByteBuffer> sessionHandler, Executor executor) throws IOException {
        super(buildServerSocketChannel(port), executor);
        this.sessionHandler = sessionHandler;
    }

    @Override
    public SelectionKey register(Selector selector) throws IOException {
        return selectableChannel.register(selector, SelectionKey.OP_ACCEPT, this);
    }

    @Override
    protected void accept(SelectionKey handle) {
        try {
            if (handle.isValid() && handle.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) handle.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                sessions.put(socketChannel, new TcpSession(this.sessionHandler));
                SelectionKey readHandle = socketChannel.register(handle.selector(), SelectionKey.OP_READ,
                        handle.attachment());
                LOGGER.debug("{} register {} with READ.", socketChannel, readHandle);
            } else {
                LOGGER.error("Unexpected handle(readyOps: {}, interestOps: {})", handle.readyOps(), handle.interestOps());
            }
        } catch (IOException e) {
            LOGGER.error("Dispatch {} accept failure.", handle, e);
        }
    }

    protected void read(SelectionKey handle) {
        Session<ByteBuffer> session = sessions.get(handle.channel());
        try {
            ByteBuffer buffer = session.read(handle);
            executor.execute(() -> session.handle(buffer, handle));
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

    public static ServerSocketChannel buildServerSocketChannel(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        return ssc;
    }
}
