package me.jameszhan.pattern.reactor.nio.event.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午5:48
 */
public interface InboundHandler {

    void read(Object readObject, SelectionKey key);

}
