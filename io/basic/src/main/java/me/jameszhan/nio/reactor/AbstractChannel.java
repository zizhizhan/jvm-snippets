package me.jameszhan.nio.reactor;

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
 * Date: 2018/10/6
 * Time: 上午12:46
 */
public abstract class AbstractChannel implements Channel {

    private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites = new ConcurrentHashMap<>();
    private final ChannelHandler handler;
    private final SelectableChannel channel;
    private Reactor reactor;

    public AbstractChannel(ChannelHandler handler, SelectableChannel channel) {
        this.handler = handler;
        this.channel = channel;
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public ChannelHandler getHandler() {
        return handler;
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    @Override
    public void flush(SelectionKey key) throws IOException {
        Queue<Object> pendingWrites = channelToPendingWrites.get(key.channel());
        while (true) {
            Object pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                // We don't have anything more to write so channel is interested in reading more data
                reactor.changeOps(key, SelectionKey.OP_READ);
                break;
            }

            // ask the concrete channel to make sense of data and write it to java channel
            doWrite(pendingWrite, key);
        }
    }

    /**
     * Queues the data for writing. The data is not guaranteed to be written on underlying channel
     * when this method returns. It will be written when the channel is flushed.
     *
     * This method is used by the {@link ChannelHandler} to send reply back to the client.
     * Example:
     *
     * <pre>
     * <code>
     * {@literal @}Override
     * public void handleChannelRead(Channel channel, Object readObject, SelectionKey key) {
     *   byte[] data = ((ByteBuffer)readObject).array();
     *   ByteBuffer buffer = ByteBuffer.wrap("Server reply".getBytes());
     *   channel.write(buffer, key);
     * }
     * </code>
     * </pre>
     *
     * @param data the data to be written on underlying channel.
     * @param key the key which is writable.
     */
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
        reactor.changeOps(key, SelectionKey.OP_WRITE);
    }

    @Override
    public void close() throws IOException {
        getSelectableChannel().close();
    }

    protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;

}
