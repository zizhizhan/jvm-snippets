package me.jameszhan.pattern.reactor;

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
        Demultiplexer demultiplexer = new Demultiplexer();
        Dispatcher dispatcher = new InitiationDispatcher(demultiplexer);
        LoggingAcceptor loggingAcceptor = new LoggingAcceptor();
        dispatcher.registerHandler(newTcpHandler(8886, loggingAcceptor))
                .registerHandler(newTcpHandler(8887, loggingAcceptor))
                .registerHandler(newUdpHandler(8888, loggingAcceptor))
                .handleEvents();
    }

    public static TcpHandler newTcpHandler(int port, Processor processor) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        LOGGER.info("Bound TCP socket at port: {}", port);
        return new TcpHandler(ssc, processor);
    }

    public static UdpHandler newUdpHandler(int port, Processor processor) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        dc.configureBlocking(false);
        LOGGER.info("Bound UDP socket at port: {}", port);
        return new UdpHandler(dc, processor);
    }

}
