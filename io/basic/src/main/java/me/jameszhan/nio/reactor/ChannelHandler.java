package me.jameszhan.nio.reactor;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/6
 * Time: 上午12:57
 */
public interface ChannelHandler {

    void handleChannelRead(Channel channel, Object readObject, SelectionKey key);

}
