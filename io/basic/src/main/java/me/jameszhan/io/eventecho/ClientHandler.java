package me.jameszhan.io.eventecho;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import me.jameszhan.io.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午2:47
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final String name;
    private final Socket socket;
    private final EventBus eventBus;

    public ClientHandler(String name, Socket socket, EventBus eventBus) {
        this.name = name;
        this.socket = socket;
        this.eventBus = eventBus;
    }

    @Subscribe
    public void receiveMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.info("{} received message {}.", name, message);
            out.println(message);
        } catch (IOException e) {
            LOGGER.error("Unexpected Error with socket {}.", socket, e);
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), IOUtils.UTF_8));
            String input;
            while ((input = in.readLine()) != null) {
                if ("exit server".equals(input)) {
                    eventBus.post(ExitEvent.DEFAULT);
                    break;
                } else {
                    eventBus.post(input);
                }
            }
            eventBus.unregister(this);
            IOUtils.close(socket);
        } catch (IOException e) {
            LOGGER.error("Unexpected Error with socket {}.", socket, e);
            eventBus.unregister(this);
        }
    }

}
