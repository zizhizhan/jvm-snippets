package me.jameszhan.nio.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 下午8:32
 */
public final class Reactors {
    private Reactors() {}

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String DEFAULT_HOST = "0.0.0.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(Reactors.class);

    public static TcpChannel newTcpChannel(int port, ChannelHandler handler) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(DEFAULT_HOST, port));
        ssc.configureBlocking(false);
        ssc.socket().setReuseAddress(true);
        LOGGER.info("Bound TCP socket at port: {}", port);
        return new TcpChannel(handler, ssc);
    }

    public static UdpChannel newUdpChannel(int port, ChannelHandler handler) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        dc.configureBlocking(false);
        LOGGER.info("Bound UDP socket at port: {}", port);
        return new UdpChannel(handler, dc);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Ignore close error.", e);
        }
    }

}