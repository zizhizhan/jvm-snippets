package me.jameszhan.pattern.reactor.simple.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午10:56
 */
public interface AcceptableChannel extends Channel {

    void accept(SelectionKey key) throws IOException;

}
