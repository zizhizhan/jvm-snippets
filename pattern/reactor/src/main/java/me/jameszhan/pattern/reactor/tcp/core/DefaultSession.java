package me.jameszhan.pattern.reactor.tcp.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:08
 */
public class DefaultSession implements Session {

    private final Queue<ByteBuffer> pendingWrites = new ConcurrentLinkedQueue<>();
    private final SessionHandler sessionHandler;

    public DefaultSession(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
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
    public void send(SelectionKey handle) throws IOException {
        while (true) {
            ByteBuffer pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                handle.interestOps(SelectionKey.OP_READ);
                handle.selector().wakeup();
                break;
            }
            ((SocketChannel) handle.channel()).write(pendingWrite);
        }
    }

    @Override
    public void write(ByteBuffer buffer, SelectionKey handle) {
        pendingWrites.add(buffer);
        handle.interestOps(SelectionKey.OP_WRITE);
        handle.selector().wakeup();
    }

    @Override
    public void handle(ByteBuffer buffer, SelectionKey handle) {
        this.sessionHandler.handle(this, buffer, handle);
    }
}
