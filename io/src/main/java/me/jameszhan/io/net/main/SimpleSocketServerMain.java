package me.jameszhan.io.net.main;

import me.jameszhan.io.util.IOUtils;
import me.jameszhan.io.net.tcp.SimpleSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/1
 * Time: 上午12:32
 */
public class SimpleSocketServerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSocketServerMain.class);
    private static final String OK_HELLO = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: 12\r\n\r\n" +
            "Hello World!";

    public static void main(String[] args) throws Exception {
        new SimpleSocketServer(8888, (in, out) -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, IOUtils.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info("-> {}", line);
                if (line.isEmpty()) {
                    break;
                }
            }
            out.write(OK_HELLO.getBytes(IOUtils.UTF_8));
            IOUtils.close(out);
            IOUtils.close(in);
        }).start();
    }

}
