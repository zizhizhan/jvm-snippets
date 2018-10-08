package me.jameszhan.pattern.reactor;

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
 * Date: 2018/10/9
 * Time: 上午12:39
 */
public abstract class AbstractHandler implements Handler {

    private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites = new ConcurrentHashMap<>();
    protected final Processor processor;
    protected final SelectableChannel channel;

    public AbstractHandler(SelectableChannel channel, Processor processor) {
        this.channel = channel;
        this.processor = processor;
    }

    public void handle(WriteEvent event) throws IOException {
        SelectionKey key = event.getData();
        Queue<Object> pendingWrites = channelToPendingWrites.get(key.channel());
        while (true) {
            Object pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                key.interestOps(SelectionKey.OP_READ);
                break;
            }
            doWrite(pendingWrite, key);
        }
    }

    public void write(Object data, SelectionKey key) {
        Queue<Object> pendingWrites = this.channelToPendingWrites.get(key.channel());
        if (pendingWrites == null) {
            synchronized (this.channelToPendingWrites) {
                pendingWrites = this.channelToPendingWrites.get(key.channel());
                if (pendingWrites == null) {
                    pendingWrites = new ConcurrentLinkedQueue<>();
                    this.channelToPendingWrites.put(key.channel(), pendingWrites);
                }
            }
        }
        pendingWrites.add(data);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;
}
