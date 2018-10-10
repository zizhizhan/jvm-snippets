package me.jameszhan.pattern.reactor.simple;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:46
 */
public class UdpChannel extends AbstractChannel {

    public UdpChannel(int port, ChannelHandler handler) throws IOException {
        super(buildDatagramChannel(port), handler);
        logger.info("Bound UDP socket at port: {}", port);
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_READ;
    }

    private static DatagramChannel buildDatagramChannel(int port) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        datagramChannel.configureBlocking(false);
        return datagramChannel;
    }

}
