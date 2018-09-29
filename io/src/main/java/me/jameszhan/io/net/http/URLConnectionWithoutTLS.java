package me.jameszhan.io.net.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:27
 */
public class URLConnectionWithoutTLS {

    public static void main(String[] args) {

        try {

            URL url = new URL("http://www.baidu.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            System.out.println("\nRequest Properties");
            print(conn.getRequestProperties());
            System.out.println("\nHeader Fields");
            print(conn.getHeaderFields());

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    public static void print(Map<String, List<String>> map) {

        for (String key : map.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(key).append(":").append("{");
            List<String> values = map.get(key);
            for (int i = 0; i < values.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(values.get(i));
            }
            sb.append("}");
            System.out.println(sb);
        }
    }

}

