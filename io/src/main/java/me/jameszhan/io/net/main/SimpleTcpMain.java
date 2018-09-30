package me.jameszhan.io.net.main;

import me.jameszhan.io.net.select.SimpleTcpClient;
import me.jameszhan.io.net.select.SimpleTcpServer;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 下午5:13
 */
public class SimpleTcpMain {

    public static void main(String[] args) throws IOException {
        int port = 8888;

        new SimpleTcpServer(port).start();
        new SimpleTcpClient("localhost", port).start();
    }

}
