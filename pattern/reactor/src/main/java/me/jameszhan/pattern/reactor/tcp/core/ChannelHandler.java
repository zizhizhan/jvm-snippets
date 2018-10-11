package me.jameszhan.pattern.reactor.tcp.core;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午10:59
 */
public interface ChannelHandler {

    void handle(Channel channel, ByteBuffer buffer, SelectionKey handle);

}
