package me.jameszhan.pattern.reactor.nio.standard.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:07
 */
public interface Session<T> {

    T read(SelectionKey handle) throws IOException;

    void send(SelectionKey handle) throws IOException;

    void write(T buffer, SelectionKey handle);

    void handle(T buffer, SelectionKey handle);

}
