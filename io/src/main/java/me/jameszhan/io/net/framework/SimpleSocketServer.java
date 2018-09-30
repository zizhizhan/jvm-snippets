package me.jameszhan.io.net.framework;

import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:52
 */
public class SimpleSocketServer {

    private static ServerSocket listenersocket;

    private static final int THREAD_POOL_TIMEOUT = 5;
    private static final int DEFAULT_MAX_CONNECTION = 5;

    private int port, maxConnections;

    private ISimpleSocketServerProtocol protocol;

    private AtomicBoolean active;

    /*
     * Threadpool is used to handle both the listener and each request handler.
     */
    private ThreadPoolExecutor threadPool;

    /**
     * Create server with default max connection.
     *
     * @param port
     *            The port number to listen on
     * @param protocol
     *            The protocol used to handle the request processing after the
     *            connection is accepted, must implement
     *            {@link ISimpleSocketServerProtocol}.
     */
    public SimpleSocketServer(int port, ISimpleSocketServerProtocol protocol) {
        this(port, DEFAULT_MAX_CONNECTION, protocol);
    }

    /**
     * Create server with default max connection.
     *
     * @param port
     *            The port number to listen on.
     * @param maxConnection
     *            The max connection the server can accept.
     * @param protocol
     *            The protocol used to handle the request processing after the
     *            connection is accepted, must implement
     *            {@link ISimpleSocketServerProtocol}.
     */
    public SimpleSocketServer(int port, int maxConnection,
                              ISimpleSocketServerProtocol protocol) {
        this.port = port;
        maxConnections = maxConnection;
        int threadPoolSize = maxConnections + 1;

        threadPool = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(threadPoolSize);

        this.protocol = protocol;

        active = new AtomicBoolean(true);
    }

    // this is such a hack, need to rewrite this later.
    public static void setListenerSocketReference(ServerSocket socket) {
        listenersocket = socket;
    }

    /**
     * Initialize the listener and allow the server to stop accepting request on
     * designated port. This method will block, will not return until everything
     * is started.
     *
     */
    public void start() throws InterruptedException {
        ConnectionRequestListener listener = new ConnectionRequestListener(
                port, maxConnections, threadPool, protocol, active);

        synchronized (listener) {
            threadPool.execute(listener);
            listener.wait();
        }
    }

    /**
     * Wait for 5 (THREAD_POOL_TIMEOUT constant) seconds and than terminate
     * thread pool operations. Which everone happens first.
     *
     */
    public void shutdown() {
        try {
            active.set(false);

            if (listenersocket != null) {
                listenersocket.close();
            }

            boolean terminated = threadPool.awaitTermination(THREAD_POOL_TIMEOUT, TimeUnit.SECONDS);
            if (!terminated) {
                System.err.println("Brute force shutdown the server.");
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println("Thread interrupted while awaiting shutdown: "
                    + ex.toString());
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * Get the number of active request the server is handling.
     *
     * @return
     */
    public int getActiveRequest() {
        return threadPool.getActiveCount() - 1;
    }

    /**
     * Return the max number of connection the server is allowed to have.
     *
     * @return
     */
    public int getMaxConnections() {
        return maxConnections;
    }
}

