package me.jameszhan.io.net.tcp;

import me.jameszhan.io.net.select.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
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
        listener.wait();
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

}

