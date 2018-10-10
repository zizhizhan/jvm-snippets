package me.jameszhan.pattern.reactor.single;

import me.jameszhan.pattern.reactor.classic.core.InboundHandler;
import me.jameszhan.pattern.reactor.classic.core.TcpEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:46
 */
public class TcpChannel extends AbstractChannel {

    public TcpChannel(int port, ChannelHandler handler) throws IOException {
        super(buildServerSocketChannel(port), handler);
        logger.info("Bound TCP socket at port: {}", port);
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_ACCEPT;
    }

    public static ServerSocketChannel buildServerSocketChannel(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        return ssc;
    }

}
