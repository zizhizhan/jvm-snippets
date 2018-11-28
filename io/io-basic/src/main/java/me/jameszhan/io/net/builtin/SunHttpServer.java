package me.jameszhan.io.net.builtin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Quick Demo of com.sun.net.httpserver.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:43
 */
public class SunHttpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunHttpServer.class);

    /**
     * curl -i http://127.0.0.1:7778/app/index.htm
     *
     * @param args main arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        HttpServerProvider httpServerProvider = HttpServerProvider.provider();
        InetSocketAddress addr = new InetSocketAddress(7778);

        HttpServer httpServer = httpServerProvider.createHttpServer(addr, 1);

        httpServer.createContext("/app/", (exchange) -> {
            LOGGER.info("Incoming: {}", toString(exchange));
            String response = "Hello World!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream out = exchange.getResponseBody();
            out.write(response.getBytes());
            out.close();
        });

        httpServer.setExecutor((cmd) -> {
            LOGGER.info("{}: {}", Thread.currentThread().getName(), cmd);
            cmd.run();
        });

        httpServer.start();
        LOGGER.info("WebServer startup.");
    }

    private static String toString(HttpExchange exchange) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(exchange.getRequestMethod()).append(' ');
        sb.append(exchange.getRequestURI()).append(' ');
        sb.append(exchange.getProtocol()).append('\n');
        exchange.getRequestHeaders().forEach((name, value) -> {
            sb.append(name).append(": ").append(value.get(0)).append('\n');
        });
        return sb.toString();
    }
}
