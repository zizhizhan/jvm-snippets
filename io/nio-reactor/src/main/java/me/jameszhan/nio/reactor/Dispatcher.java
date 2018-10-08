package me.jameszhan.nio.reactor;

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

    /**
     * Stops dispatching events and cleans up any acquired resources such as threads.
     *
     * @throws InterruptedException if interrupted while stopping dispatcher.
     */
    void stop() throws InterruptedException;

}
