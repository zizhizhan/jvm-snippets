package me.jameszhan.pattern.reactor.nio.simple.main;

import me.jameszhan.pattern.reactor.nio.simple.core.ChannelHandler;
import me.jameszhan.pattern.reactor.nio.simple.core.Reactor;
import me.jameszhan.pattern.reactor.nio.simple.core.TcpChannel;
import me.jameszhan.pattern.reactor.nio.simple.core.UdpChannel;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:46
 */
public class ReactorServer {

    public static void main(String[] args) throws IOException {
        ChannelHandler handler = new LoggingHandler();
        Reactor reactor = new Reactor(0);
        reactor.register(new TcpChannel(8886, handler))
                .register(new TcpChannel(8887, handler))
                .register(new UdpChannel(8888, handler))
                .register(new UdpChannel(8889, handler))
                .start();
    }
}
