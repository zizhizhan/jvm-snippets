package me.jameszhan.io.net.main;

import me.jameszhan.io.net.select.UdpEchoClient;
import me.jameszhan.io.net.select.UdpEchoServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 下午2:28
 */
public class UdpEchoMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpEchoMain.class);

    public static void main(String[] args) throws Exception {
        int port = 8888;
        new Thread(() -> startServer(port)).start();

        new UdpEchoClient("localhost", port).start();
    }

    private static void startServer(int port) {
        UdpEchoServer udpEchoServer = new UdpEchoServer(port);
        try {
            udpEchoServer.execute();
        } catch (IOException e) {
            LOGGER.info("Unexpected Server Error.", e);
        }
    }

}
