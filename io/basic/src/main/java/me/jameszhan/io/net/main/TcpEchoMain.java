package me.jameszhan.io.net.main;

import me.jameszhan.io.net.select.TcpEchoClient;
import me.jameszhan.io.net.select.TcpEchoServer;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 下午10:35
 */
public class TcpEchoMain {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        new TcpEchoServer(port).start();
        new TcpEchoClient("localhost", port).start();
    }

}
