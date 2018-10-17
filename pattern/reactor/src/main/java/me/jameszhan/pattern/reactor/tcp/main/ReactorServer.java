package me.jameszhan.pattern.reactor.tcp.main;

import me.jameszhan.pattern.reactor.tcp.core.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午10:13
 */
public class ReactorServer {

    /**
     * telnet localhost 8888
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SessionDispatcher dispatcher = new SessionDispatcher(0);
        Reactor subReactor = new Reactor(dispatcher);

        AcceptDispatcher acceptor = new AcceptDispatcher(subReactor);
        Reactor reactor = new Reactor(acceptor);

        SessionHandler sessionHandler = new LoggingHandler();
        reactor.register(buildServerSocketChannel(8888), SelectionKey.OP_ACCEPT, sessionHandler).start();
        subReactor.start();
    }

    private static ServerSocketChannel buildServerSocketChannel(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);
        return channel;
    }

}
