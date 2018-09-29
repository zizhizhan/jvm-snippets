package me.jameszhan.io.net.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class HandleHttpHeaders {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleHttpHeaders.class);

    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.baidu.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            LOGGER.info("Request: \n{} {}", conn.getRequestMethod(), conn.getURL());
            LOGGER.info("Request Properties: {}", toString(conn.getRequestProperties()));

            LOGGER.info("Response: \n{} {}", conn.getResponseCode(), conn.getResponseMessage());
            LOGGER.info("Header Fields: {}", toString(conn.getHeaderFields()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toString(Map<String, List<String>> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append('\n').append(key).append(": ").append("{ ");
            List<String> values = map.get(key);
            for (int i = 0; i < values.size(); i++) {
                if (i != 0) {
                    sb.append(" | ");
                }
                sb.append(values.get(i));
            }
            sb.append(" }");
        }
        return sb.toString();
    }

}

