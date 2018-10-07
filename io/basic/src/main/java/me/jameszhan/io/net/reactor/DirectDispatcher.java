package me.jameszhan.io.net.reactor;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/6
 * Time: 上午12:49
 */
public class DirectDispatcher implements Dispatcher {

    @Override
    public void onChannelReadEvent(Channel channel, Object readObject, SelectionKey key) {
        channel.getHandler().handleChannelRead(channel, readObject, key);
    }

}
