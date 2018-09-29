package me.jameszhan.dirty.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:59
 */
class ConnectionRequestListener implements Runnable {
    int port;
    int maxConnection;

    ThreadPoolExecutor threadPool;
    ServerSocket listener;
    AtomicBoolean active;

    ISimpleSocketServerProtocol protocol;

    public ConnectionRequestListener(int port, int maxConnection,
                                     ThreadPoolExecutor threadPool,
                                     ISimpleSocketServerProtocol protocol, AtomicBoolean active) {
        this.port = port;
        this.maxConnection = maxConnection;
        this.threadPool = threadPool;
        this.protocol = protocol;

        this.active = active;
    }

    public void run() {
        Socket socket = null;
        try {
            listener = new ServerSocket(port);

            SimpleSocketServer.setListenerSocketReference(listener);

            synchronized (this) {
                this.notify();
            }
        } catch (IOException ex) {
            System.err.println("Failed to listen on specified port: " + port
                    + "\r\n" + ex.toString());
            return;
        }

        try {
            while (active.get()) {
                socket = listener.accept();
                ConnectionHandler connection = new ConnectionHandler(socket,
                        protocol);
                threadPool.execute(connection);
            }

            listener.close();
        } catch (IOException ex) {
            System.err
                    .println("Error accepting connection, request ignored. \r\n"
                            + ex.toString());
        }

    }
}
