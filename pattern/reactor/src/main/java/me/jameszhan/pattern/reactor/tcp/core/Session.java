package me.jameszhan.pattern.reactor.tcp.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:07
 */
public interface Session {

    ByteBuffer read(SelectionKey handle) throws IOException;

    void send(SelectionKey handle) throws IOException;

    void write(ByteBuffer buffer, SelectionKey handle);

    void handle(ByteBuffer buffer, SelectionKey handle);

}
