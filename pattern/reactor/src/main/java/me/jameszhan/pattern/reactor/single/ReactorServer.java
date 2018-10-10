package me.jameszhan.pattern.reactor.single;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:46
 */
public class ReactorServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorServer.class);

    public static void main(String[] args) throws IOException {
        ChannelHandler handler = new LoggingHandler();
        Reactor reactor = new Reactor(0);
        reactor.register(new TcpChannel(8886, handler))
                .register(new UdpChannel(8887, handler))
                .register(new UdpChannel(8888, handler))
                .start();
    }
}
