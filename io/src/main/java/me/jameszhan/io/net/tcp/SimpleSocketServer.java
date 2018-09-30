package me.jameszhan.io.net.tcp;

import me.jameszhan.io.net.select.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/9
 * Time: PM11:52
 */
public class SimpleSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSocketServer.class);

    private static ServerSocket serverSocket;

    private static final int THREAD_POOL_TIMEOUT = 5;

    private int port;
    private IOProtocol protocol;
    private AtomicBoolean active;

    /*
     * Threadpool is used to handle both the listener and each request handler.
     */
    private Executor threadPool;

    /**
     * Create server with default max connection.
     *
     * @param port          The port number to listen on.
     * @param protocol      The protocol used to handle the request processing after the
     *                      connection is accepted, must implement
     *                      {@link IOProtocol}.
     */
    public SimpleSocketServer(int port, IOProtocol protocol) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(20);
        this.protocol = protocol;
        this.active = new AtomicBoolean(false);
    }

    /**
     * Initialize the listener and allow the server to stop accepting request on
     * designated port. This method will block, will not return until everything
     * is started.
     */
    public void start() throws InterruptedException {
        ConnectionListener listener = new ConnectionListener(port, threadPool, protocol, active);

        threadPool.execute(listener);
    }

    /**
     * Wait for 5 (THREAD_POOL_TIMEOUT constant) seconds and than terminate
     * thread pool operations. Which everone happens first.
     */
    public void shutdown() {
        try {
            active.set(false);
            IOUtils.close(serverSocket);

            boolean terminated = ((ThreadPoolExecutor)threadPool).awaitTermination(THREAD_POOL_TIMEOUT, TimeUnit.SECONDS);
            if (!terminated) {
                LOGGER.info("Brute force shutdown the server.");
                ((ThreadPoolExecutor)threadPool).shutdownNow();
            }
        } catch (InterruptedException ex) {
            LOGGER.error("Thread interrupted while awaiting shutdown: ", ex);
        }
    }

    // this is such a hack, need to rewrite this later.
    static void setServerSocket(ServerSocket socket) {
        serverSocket = socket;
    }

    /**
     * Created with IntelliJ IDEA.
     *
     * @author zizhi.zhzzh
     *         Date: 16/3/9
     *         Time: PM11:52
     */
    public interface IOProtocol {
        void handle(InputStream in, OutputStream out) throws IOException;
    }

    /**
     * Created with IntelliJ IDEA.
     *
     * @author zizhi.zhzzh
     *         Date: 16/3/9
     *         Time: PM11:58
     */
    private static class ConnectionHandler implements Runnable {

        private Socket socket;
        private IOProtocol protocol;

        /**
         * Will get the input and output stream from socket, and setup the reader
         * and writer.
         *
         * @param socket
         */
        ConnectionHandler(Socket socket, IOProtocol protocol) {
            this.socket = socket;
            this.protocol = protocol;
        }

        /**
         * Continuously process input and output from the socket until a termination code is sent.
         *
         */
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                protocol.handle(in, out);
            } catch (IOException e) {
                LOGGER.error("Unexpected IO Error.", e);
            }
        }

        /**
         * Close the reader and writer first, than close off the socket.
         *
         */
        public void close() {
            IOUtils.close(socket);
            socket = null;
        }
    }

    /**
     * Created with IntelliJ IDEA.
     *
     * @author zizhi.zhzzh
     * Date: 16/3/9
     * Time: PM11:59
     */
    private static class ConnectionListener implements Runnable {

        private int port;

        private Executor threadPool;
        private ServerSocket serverSocket;
        private AtomicBoolean active;

        private IOProtocol protocol;

        ConnectionListener(int port, Executor threadPool, IOProtocol protocol, AtomicBoolean active) {
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
}

