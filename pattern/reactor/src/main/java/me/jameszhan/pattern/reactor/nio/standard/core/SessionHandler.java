package me.jameszhan.pattern.reactor.nio.standard.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:26
 */
public interface SessionHandler<T> {

    void handle(Session<T> channel, T buffer, SelectionKey handle);

}
