package me.jameszhan.pattern.reactor.simple.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午12:29
 */
public abstract class AbstractChannel implements Channel {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<SelectableChannel, Queue<Message>> channelToPendingWrites = new ConcurrentHashMap<>();
    private final SelectableChannel channel;
    private final ChannelHandler handler;

    public AbstractChannel(SelectableChannel channel, ChannelHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    @Override
    public void handle(Message message, SelectionKey key) {
        handler.handle(this, message, key);
    }

    @Override
    public void enqueue(Message message, SelectionKey key) {
        Queue<Message> pendingWrites = this.channelToPendingWrites.get(key.channel());
        if (pendingWrites == null) {
            synchronized (this.channelToPendingWrites) {
                pendingWrites = this.channelToPendingWrites.get(key.channel());
                if (pendingWrites == null) {
                    pendingWrites = new ConcurrentLinkedQueue<>();
                    this.channelToPendingWrites.put(key.channel(), pendingWrites);
                }
            }
        }
        pendingWrites.add(message);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    @Override
    public void write(SelectionKey key) throws IOException {
        Queue<Message> pendingWrites = this.channelToPendingWrites.get(key.channel());
        while (true) {
            Message pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                key.interestOps(SelectionKey.OP_READ);
                break;
            }

            doWrite(pendingWrite, key);
        }
    }

    protected abstract void doWrite(Message message, SelectionKey key) throws IOException;

}
