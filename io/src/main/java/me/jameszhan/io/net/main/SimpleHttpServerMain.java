package me.jameszhan.io.net.main;

import me.jameszhan.io.net.http.SimpleHttpServer;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/29
 * Time: 下午6:58
 */
public class SimpleHttpServerMain {

    public static void main(String[] args) throws IOException {
        SimpleHttpServer server = new SimpleHttpServer();
        server.startup();
    }

}
