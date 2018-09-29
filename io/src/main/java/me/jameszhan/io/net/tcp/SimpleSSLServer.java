package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/10
 * Time: AM1:21
 */
public class SimpleSSLServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSSLServer.class);
    private static final String PASSPHRASE = "password";
    static final int PORT = 8086;

    public static void main(String[] args) throws IOException {
        SSLContext sslContext = buildSSLContext(PASSPHRASE);
        if (sslContext != null) {
            ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            AtomicBoolean running = new AtomicBoolean(true);
            try (ServerSocket serverSocket = serverSocketFactory.createServerSocket(PORT)) {
                LOGGER.info("Secure socket created. Listening on {}.", PORT);
                while (running.get()) {
                    try (Socket socket = serverSocket.accept()) {
                        LOGGER.info("Accepting: {}.", socket);
                        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                            String request = in.readUTF();
                            LOGGER.info("Get {}.", request);
                            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                                out.writeUTF("Echo: " + request);
                                out.flush();
                            }
                        } catch (IOException e) {
                            LOGGER.error("Handle {} with exception {}.", socket, e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    private static SSLContext buildSSLContext(String passphrase) {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");

            char[] password = passphrase.toCharArray();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            keyStore.load(classLoader.getResourceAsStream("server.keystore"), password);
            keyManagerFactory.init(keyStore, password);
            context.init(keyManagerFactory.getKeyManagers(), null, null);
            return context;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException
                | KeyManagementException | UnrecoverableKeyException e) {
            LOGGER.error("Can't initialize SSLContext", e);
            return null;
        }
    }
}
