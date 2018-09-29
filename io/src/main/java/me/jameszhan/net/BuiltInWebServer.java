package me.jameszhan.net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:43
 */
public class BuiltInWebServer {

    /**
     * curl http://127.0.0.1:7778/app/index.htm
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        HttpServerProvider httpServerProvider = HttpServerProvider.provider();
        InetSocketAddress addr = new InetSocketAddress(7778);
        HttpServer httpServer = httpServerProvider.createHttpServer(addr, 1);
        httpServer.createContext("/app/", new HttpHandler(){
            public void handle(HttpExchange httpExchange) throws IOException {
                String response = "Hello world!";
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream out = httpExchange.getResponseBody();
                out.write(response.getBytes());
                out.close();
            }
        });
        httpServer.setExecutor(new Executor() {
            @Override
            public void execute(Runnable command) {
                System.out.println("Command Run!");
                command.run();
            }
        });
        httpServer.start();
        System.out.println("started");
    }
}
