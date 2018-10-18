package me.jameszhan.pattern.reactor.nio.event.core;

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

    Object handle(ReadEvent e) throws IOException;

    void handle(WriteEvent e) throws IOException;

    InboundHandler getInboundHandler();

    void write(Object data, SelectionKey key);

    SelectableChannel getSelectableChannel();

    int interestOps();

}
