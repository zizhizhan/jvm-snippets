package me.jameszhan.pattern.reactor.nio.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:08
 */
public class TcpSession extends AbstractSession<ByteBuffer> {

    public TcpSession(SessionHandler<ByteBuffer> sessionHandler) {
        super(sessionHandler);
    }

    @Override
    public ByteBuffer read(SelectionKey handle) throws IOException {
        SocketChannel socketChannel = (SocketChannel) handle.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = socketChannel.read(buffer);
        buffer.flip();
        if (length == -1) {
            throw new EOFException("Socket closed");
        }
        return buffer;
    }

    @Override
    protected void doWrite(ByteBuffer pendingWrite, SelectionKey handle) throws IOException {
        ((SocketChannel) handle.channel()).write(pendingWrite);
    }

}
