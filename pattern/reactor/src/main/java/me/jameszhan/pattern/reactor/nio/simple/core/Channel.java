package me.jameszhan.pattern.reactor.nio.simple.core;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:43
 */
public interface Channel {

    SelectableChannel getSelectableChannel();

    int interestOps();

    void handle(Message message, SelectionKey handle);

    void enqueue(Message message, SelectionKey handle);

    Message read(SelectionKey handle) throws IOException;

    void write(SelectionKey handle) throws IOException;

}
