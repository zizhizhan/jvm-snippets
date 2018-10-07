package me.jameszhan.io.net.reactor;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

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

    protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;

}
