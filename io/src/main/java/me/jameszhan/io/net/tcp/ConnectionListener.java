package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/9
 * Time: PM11:59
 */
class ConnectionListener implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionListener.class);

    private int port;

    private Executor threadPool;
    private ServerSocket serverSocket;
    private AtomicBoolean active;

    private IOProtocol protocol;

    public ConnectionListener(int port, Executor threadPool, IOProtocol protocol, AtomicBoolean active) {
        this.port = port;
        this.threadPool = threadPool;
        this.protocol = protocol;
        this.active = active;
    }

    public void run() {
        initialize();
        try {
            while (active.get()) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ConnectionHandler(socket, protocol));
            }
            serverSocket.close();
        } catch (IOException ex) {
            LOGGER.info("Error accepting connection, request ignored.", ex);
        }

    }

    private void initialize() {
        try {
            serverSocket = new ServerSocket(port);
            SimpleSocketServer.setServerSocket(serverSocket);
            synchronized (this) {
                this.notifyAll();
            }
            active.compareAndSet(false, true);
        } catch (IOException ex) {
            LOGGER.info("Failed to listen on specified port: {}.", ex);
            active.compareAndSet(true, false);
        }
    }
}
