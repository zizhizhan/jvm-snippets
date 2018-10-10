package me.jameszhan.pattern.reactor.simple.core;

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

    void handle(Message message, SelectionKey key);

    void enqueue(Message message, SelectionKey key);

    Message read(SelectionKey key) throws IOException;

    void write(SelectionKey key) throws IOException;

}
