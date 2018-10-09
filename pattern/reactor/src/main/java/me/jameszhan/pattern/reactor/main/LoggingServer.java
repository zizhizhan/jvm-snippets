package me.jameszhan.pattern.reactor.main;

import me.jameszhan.pattern.reactor.Processor;
import me.jameszhan.pattern.reactor.core.Dispatcher;
import me.jameszhan.pattern.reactor.core.InitiationDispatcher;
import me.jameszhan.pattern.reactor.core.TcpEventHandler;
import me.jameszhan.pattern.reactor.core.UdpEventHandler;
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
 * Date: 2018/10/8
 * Time: 下午8:21
 */
public class LoggingServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServer.class);

    public static void main(String[] args) throws IOException {
        Dispatcher dispatcher = new InitiationDispatcher();
        LoggingAcceptor loggingAcceptor = new LoggingAcceptor();
        dispatcher.registerHandler(newTcpHandler(8886, loggingAcceptor))
                .registerHandler(newTcpHandler(8887, loggingAcceptor))
                .registerHandler(newUdpHandler(8888, loggingAcceptor))
                .handleEvents();
    }

    public static TcpEventHandler newTcpHandler(int port, Processor processor) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        LOGGER.info("Bound TCP socket at port: {}", port);
        return new TcpEventHandler(ssc, processor);
    }

    public static UdpEventHandler newUdpHandler(int port, Processor processor) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        dc.configureBlocking(false);
        LOGGER.info("Bound UDP socket at port: {}", port);
        return new UdpEventHandler(dc, processor);
    }

}
