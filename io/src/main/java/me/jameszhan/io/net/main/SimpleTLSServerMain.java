package me.jameszhan.io.net.main;

import me.jameszhan.io.net.tcp.SimpleTLSClient;
import me.jameszhan.io.net.tcp.SimpleTLSServer;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/29
 * Time: 下午11:25
 */
public class SimpleTLSServerMain {

    public static void main(String[] args) throws IOException {
        int port = 8086;
        SimpleTLSClient client = new SimpleTLSClient("127.0.0.1", port);
        new SimpleTLSServer(port, 10, "password".toCharArray(), true).start((running) -> {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println(client.invoke("Request-" + i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            running.compareAndSet(true, false);
            try {
                client.invoke("STOP SERVER");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
