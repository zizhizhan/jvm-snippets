package me.jameszhan.pattern.reactor.core;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:34
 */
public interface EventHandler {

    void handle(AcceptEvent e) throws IOException;

    void handle(ReadEvent e) throws IOException;

    void handle(WriteEvent e) throws IOException;

    void write(Object data, SelectionKey key);

    SelectableChannel getSelectableChannel();

    int interestOps();

}
