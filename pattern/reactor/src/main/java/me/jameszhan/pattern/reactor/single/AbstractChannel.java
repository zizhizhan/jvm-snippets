package me.jameszhan.pattern.reactor.single;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午12:29
 */
public abstract class AbstractChannel implements Channel {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
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
    public void handle(Message message) {
        handler.handle(this, message);
    }

}
