package me.jameszhan.nio.reactor;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/6
 * Time: 上午12:30
 */
public class TcpChannel extends AbstractChannel {

    public TcpChannel(ChannelHandler handler, SelectableChannel channel) {
        super(handler, channel);
    }

    public SocketChannel accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        return socketChannel;
    }

    @Override
    public Object read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = socketChannel.read(buffer);
        buffer.flip();
        if (length == -1) {
            throw new EOFException("Socket closed");
        }
        return buffer;
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_ACCEPT;
    }

    /**
     * Writes the pending {@link ByteBuffer} to the underlying channel sending data to the intended
     * receiver of the packet.
     */
    @Override
    protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
        ByteBuffer pendingBuffer = (ByteBuffer) pendingWrite;
        ((SocketChannel) key.channel()).write(pendingBuffer);
    }
}
