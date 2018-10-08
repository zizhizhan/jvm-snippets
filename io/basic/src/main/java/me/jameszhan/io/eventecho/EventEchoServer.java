package me.jameszhan.io.eventecho;

import com.google.common.collect.Lists;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import me.jameszhan.io.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午3:04
 */
public class EventEchoServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventEchoServer.class);

    public static void main(String[] args) throws IOException {
        ExecutorService clientExecutor = Executors.newCachedThreadPool();
        ExecutorService eventExecutor = Executors.newSingleThreadExecutor();

        EventBus eventBus = new AsyncEventBus("EchoServer", eventExecutor);
        Thread mainThread = Thread.currentThread();
        ServerSocket serverSocket = new ServerSocket(8888);
        eventBus.register(new Object(){
            @Subscribe public void exit(ExitEvent event) {
                LOGGER.info("Server[{}] received exit command {}.", mainThread.isInterrupted(), event);
                mainThread.interrupt();
                IOUtils.close(serverSocket);
            }
        });
        List<Socket> sockets = Lists.newArrayList();
        int i = 0;
        while (!mainThread.isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                LOGGER.info("Server[{}] accept new client {}.", mainThread.isInterrupted(), socket);
                ClientHandler handler = new ClientHandler(String.format("client-%02d", ++i), socket, eventBus);
                eventBus.register(handler);
                clientExecutor.execute(handler);
            } catch (SocketException e) {
                LOGGER.info("ServerSocket {} is closed.", serverSocket, e);
                break;
            }
        }

        LOGGER.info("Server prepare to down.");
        sockets.forEach(IOUtils::close);
        clientExecutor.shutdownNow();
        eventExecutor.shutdown();
    }
}
