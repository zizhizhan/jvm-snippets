package me.jameszhan.io.net.main;

import me.jameszhan.io.net.select.SimpleNIOHttpClient;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 上午11:24
 */
public class SimpleNIOHttpClientMain {

    public static void main(String[] args) throws IOException {
        SimpleNIOHttpClient client = new SimpleNIOHttpClient();
        System.out.println(client.grab("http://www.baidu.com"));
    }

}
