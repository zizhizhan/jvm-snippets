package me.jameszhan.pattern.reactor.tcp.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午10:56
 */
public class DefaultChannel implements Channel {

    private final Map<SelectableChannel, Queue<ByteBuffer>> channelToPendingWrites = new ConcurrentHashMap<>();
    private final ChannelHandler handler;

    public DefaultChannel(ChannelHandler handler) throws IOException {
        this.handler = handler;
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
    public void write(ByteBuffer buffer, SelectionKey handle) {
        Queue<ByteBuffer> pendingWrites = this.channelToPendingWrites.get(handle.channel());
        if (pendingWrites == null) {
            synchronized (this.channelToPendingWrites) {
                pendingWrites = this.channelToPendingWrites.get(handle.channel());
                if (pendingWrites == null) {
                    pendingWrites = new ConcurrentLinkedQueue<>();
                    this.channelToPendingWrites.put(handle.channel(), pendingWrites);
                }
            }
        }
        pendingWrites.add(buffer);
        handle.interestOps(SelectionKey.OP_WRITE);
        handle.selector().wakeup();
    }

    @Override
    public void send(SelectionKey handle) throws IOException {
        Queue<ByteBuffer> pendingWrites = channelToPendingWrites.get(handle.channel());
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
    public void handle(ByteBuffer buffer, SelectionKey handle) {
        this.handler.handle(this, buffer, handle);
    }

}
