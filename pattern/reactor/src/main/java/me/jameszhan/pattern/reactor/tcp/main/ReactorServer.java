package me.jameszhan.pattern.reactor.tcp.main;

import me.jameszhan.pattern.reactor.tcp.core.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午10:13
 */
public class ReactorServer {

    public static void main(String[] args) throws IOException {
        SelectorProvider selectorProvider = SelectorProvider.provider();
        Selector selector = selectorProvider.openSelector();
        ReadWriteDispatcher dispatcher = new ReadWriteDispatcher(0);
        Reactor subReactor = new Reactor("SubReactor", dispatcher, selector);

        AcceptDispatcher acceptor = new AcceptDispatcher(subReactor);
        Reactor reactor = new Reactor("MainReactor", acceptor, selector);

        Channel channel = new DefaultChannel(new LoggingHandler());
        reactor.register(buildServerSocketChannel(selectorProvider,8888), SelectionKey.OP_ACCEPT, channel).start();
        subReactor.start();
    }

    private static ServerSocketChannel buildServerSocketChannel(SelectorProvider selectorProvider, int port) throws IOException {
        ServerSocketChannel channel = selectorProvider.openServerSocketChannel(); // ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);
        return channel;
    }

}
