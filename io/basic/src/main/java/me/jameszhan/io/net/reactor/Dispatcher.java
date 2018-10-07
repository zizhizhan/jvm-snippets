package me.jameszhan.io.net.reactor;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/5
 * Time: 下午9:10
 */
public interface Dispatcher {

    void onChannelReadEvent(Channel channel, Object readObject, SelectionKey key);

}
