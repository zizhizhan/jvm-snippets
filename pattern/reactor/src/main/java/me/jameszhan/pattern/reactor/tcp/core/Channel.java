package me.jameszhan.pattern.reactor.tcp.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午8:39
 */
public interface Channel {

    ByteBuffer read(SelectionKey handle) throws IOException;

    void write(ByteBuffer buffer, SelectionKey handle);

    void send(SelectionKey handle) throws IOException;

    void handle(ByteBuffer buffer, SelectionKey handle);

}
