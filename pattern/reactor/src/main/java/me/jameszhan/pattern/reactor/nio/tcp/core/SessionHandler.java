package me.jameszhan.pattern.reactor.nio.tcp.core;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:26
 */
public interface SessionHandler {

    void handle(Session channel, ByteBuffer buffer, SelectionKey handle);

}
