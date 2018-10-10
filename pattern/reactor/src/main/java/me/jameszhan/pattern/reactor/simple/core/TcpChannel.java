package me.jameszhan.pattern.reactor.simple.core;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:46
 */
public class TcpChannel extends AbstractChannel implements AcceptableChannel {

    public TcpChannel(int port, ChannelHandler handler) throws IOException {
        super(buildServerSocketChannel(port), handler);
        logger.info("Bound TCP socket at port: {}", port);
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_ACCEPT;
    }

    @Override
    public void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        SelectionKey readKey = socketChannel.register(key.selector(), SelectionKey.OP_READ, key.attachment());
        logger.debug("Accept {} with {}.", socketChannel, readKey);
    }

    @Override
    public Message read(SelectionKey handle) throws IOException {
        SocketChannel socketChannel = (SocketChannel) handle.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = socketChannel.read(buffer);
        buffer.flip();
        if (length == -1) {
            throw new EOFException("Socket closed");
        }
        return new Message(buffer, socketChannel.getRemoteAddress());
    }

    @Override
    protected void doWrite(Message message, SelectionKey key) throws IOException {
        ((SocketChannel) key.channel()).write(message.buffer);
    }

    public static ServerSocketChannel buildServerSocketChannel(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        return ssc;
    }

}
