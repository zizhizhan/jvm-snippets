package me.jameszhan.nio.app;

import me.jameszhan.nio.reactor.*;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/7
 * Time: 下午8:43
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        Dispatcher dispatcher = new ThreadDispatcher(Runtime.getRuntime().availableProcessors());
        Reactor reactor = new Reactor(dispatcher);
        ChannelHandler loggingHandler = new LoggingHandler();
        reactor.register(Reactors.newTcpChannel(8887, loggingHandler))
                .register(Reactors.newTcpChannel(8888, loggingHandler))
                .register(Reactors.newUdpChannel(8889, loggingHandler))
                .start();

        Thread.sleep(180000);
        reactor.stop();
    }

}
