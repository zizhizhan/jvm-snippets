package me.jameszhan.io.net.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM1:26
 */
public class HttpClientViaURLConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientViaURLConnection.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public static void main(String[] args) throws IOException {
        System.out.println(grab("https://www.baidu.com", "GET", ""));
    }

    private static String grab(String url, String method, String content) throws IOException {
        URL requestURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestProperty("User-Agent", "HttpClientViaURLConnection/1.0");
        connection.setRequestMethod(method);

        initializeConnection(connection, content, "text/html", 100);

        InputStreamReader isr = null;
        try {
            connection.connect();
            try (OutputStream out = connection.getOutputStream()) {
                out.write(content.getBytes(UTF_8));
            }
            isr = new InputStreamReader(connection.getInputStream(), UTF_8);
        } catch (ConnectException e) {
            LOGGER.info("Error: Could not connect to {}, is the http server alive?\n", url, e);
        } catch (IOException e) {
            LOGGER.info("Error", e);
            isr = new InputStreamReader(connection.getErrorStream(), UTF_8);
        }

        if (isr != null) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                return sb.toString();
            }
        } else {
            return null;
        }
    }

    private static void initializeConnection(HttpURLConnection connection, String content, String contentType, int timeout) {
        if (!"GET".equals(connection.getRequestMethod())) {
            connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes(UTF_8).length));
            connection.setRequestProperty("Content-Type", contentType);
        }

        // necessary for Connection: Keep-Alive, but not that useful for Connection: close
        connection.setReadTimeout(timeout);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

}
