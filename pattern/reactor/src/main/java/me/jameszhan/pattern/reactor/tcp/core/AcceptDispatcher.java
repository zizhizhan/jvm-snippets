package me.jameszhan.pattern.reactor.tcp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午8:31
 */
public class AcceptDispatcher implements Dispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptDispatcher.class);
    private final Reactor subReactor;

    public AcceptDispatcher(Reactor subReactor) {
        this.subReactor = subReactor;
    }

    @Override
    public void dispatch(SelectionKey handle) {
        try {
            if (handle.isValid() && handle.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) handle.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                Channel channel = (Channel) handle.attachment();
                subReactor.register(socketChannel, SelectionKey.OP_READ, channel);
            } else {
                LOGGER.error("Unexpected handle(readyOps: {}, interestOps: {})", handle.readyOps(), handle.interestOps());
            }
        } catch (IOException e) {
            LOGGER.error("Dispatch {} accept failure.", handle, e);
        }
    }

}
