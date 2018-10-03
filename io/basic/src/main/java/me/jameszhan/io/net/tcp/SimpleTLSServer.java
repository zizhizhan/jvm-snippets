package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/10
 * Time: AM12:47
 */
public class SimpleTLSServer {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleTLSServer.class);

    private Executor executor;
    private Semaphore semaphore;
    private AtomicBoolean isRunning;
    private int port;
    private int backlog;
    private char[] passphrase;
    private boolean useTLS;

    public SimpleTLSServer(int port, int backlog, char[] passphrase, boolean useTLS) {
        super();
        this.isRunning = new AtomicBoolean(true);
        this.port = port;
        this.backlog = backlog;
        this.passphrase = passphrase;
        this.useTLS = useTLS;
        this.semaphore = new Semaphore(backlog);
        this.executor = Runnable::run;
    }

    public void start(Consumer<AtomicBoolean> consumer) throws IOException {
        ServerSocketFactory ssf = getServerSocketFactory();
        try (ServerSocket serverSocket = ssf.createServerSocket(port, backlog)) {
            LOGGER.info("Server begin listening: {} with backlog {}", port, backlog);
            if (consumer != null) {
                new Thread(() -> consumer.accept(isRunning)).start();
            }

            while (isRunning.get()) {
                try (Socket socket = serverSocket.accept()) {
                    LOGGER.info("Accepting: {}.", socket);
                    try {
                        socket.setSoTimeout(60 * 1000);
                        process(socket);
                    } catch (Exception e) {
                        LOGGER.error("Handle {} with exception {}.", socket, e.getMessage(), e);
                    }
                }
            }
        }
    }

    private void process(Socket socket) {
        semaphore.acquireUninterruptibly();
        try {
            executor.execute(() -> handle(socket));
        } finally {
            semaphore.release();
        }
    }

    private void handle(Socket socket) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            String request = in.readUTF();
            LOGGER.info("Received {}.", request);
            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                out.writeUTF("Echo: " + request);
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error("Handle {} with exception {}.", socket, e.getMessage(), e);
        }
    }

    private ServerSocketFactory getServerSocketFactory() {
        ServerSocketFactory factory;
        if (useTLS) {
            SSLContext sslContext = buildSSLContext("server.keystore", passphrase);
            if (sslContext != null) {
                factory = sslContext.getServerSocketFactory();
            } else {
                factory = ServerSocketFactory.getDefault();
            }
        } else {
            factory = ServerSocketFactory.getDefault();
        }
        return factory;
    }

    private static SSLContext buildSSLContext(String keyStoreFile, char[] password) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = createKeyManagerFactory(keyStoreFile, password);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            return sslContext;
        } catch (Exception e) {
            LOGGER.info("Can't build SSLContext.", e);
            return null;
        }
    }

    static KeyManagerFactory createKeyManagerFactory(String keyStoreFile, char[] password) throws KeyStoreException,
            UnrecoverableKeyException, NoSuchAlgorithmException, IOException, CertificateException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        keyStore.load(classLoader.getResourceAsStream(keyStoreFile), password);

        keyManagerFactory.init(keyStore, password);
        return keyManagerFactory;
    }

}
