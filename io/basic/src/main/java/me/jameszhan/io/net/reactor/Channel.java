package me.jameszhan.io.net.reactor;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/5
 * Time: 下午9:14
 */
public interface Channel extends Closeable {

    ChannelHandler getHandler();

    SocketChannel accept(SelectionKey key) throws IOException;

    Object read(SelectionKey key) throws IOException;

    void flush(SelectionKey key) throws IOException;

    SelectableChannel getSelectableChannel();

    int interestOps();

}
