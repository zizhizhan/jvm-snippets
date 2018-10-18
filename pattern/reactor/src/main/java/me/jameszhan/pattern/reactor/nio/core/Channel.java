package me.jameszhan.pattern.reactor.nio.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:58
 */
public interface Channel {

    void dispatch(SelectionKey handle);

    SelectionKey register(Selector selector) throws IOException ;

}
