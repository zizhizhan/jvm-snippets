package me.jameszhan.pattern.reactor.nio.tcp.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午8:28
 */
public interface Dispatcher {

    void dispatch(SelectionKey handle);

}
