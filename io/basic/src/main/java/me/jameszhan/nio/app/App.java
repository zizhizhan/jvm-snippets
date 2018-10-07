package me.jameszhan.nio.app;

import me.jameszhan.nio.reactor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/7
 * Time: 下午8:43
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String DEFAULT_HOST = "0.0.0.0";

    public static void main(String[] args) throws IOException {
        Dispatcher dispatcher = new ThreadDispatcher(Runtime.getRuntime().availableProcessors());
        Reactor reactor = new Reactor(dispatcher);
        ChannelHandler loggingHandler = new LoggingHandler();
        reactor.register(tcpChannel(8887, loggingHandler))
                .register(tcpChannel(8888, loggingHandler))
                .register(udpChannel(8889, loggingHandler))
                .start();
    }

    private static TcpChannel tcpChannel(int port, ChannelHandler handler) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(DEFAULT_HOST, port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        LOGGER.info("Bound TCP socket at port: {}", port);
        return new TcpChannel(handler, ssc);
    }

    private static UdpChannel udpChannel(int port, ChannelHandler handler) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        dc.configureBlocking(false);
        LOGGER.info("Bound UDP socket at port: {}", port);
        return new UdpChannel(handler, dc);
    }

}
