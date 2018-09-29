package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM1:18
 */
public class SimpleSSLClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSSLClient.class);

    public static void main(String[] args) throws IOException {
        char[] password = "password".toCharArray();
        SSLContext sslContext = buildSSLContext(password);
        if (sslContext != null) {
            SSLSocketFactory factory = sslContext.getSocketFactory();
            try (SSLSocket socket = (SSLSocket) factory.createSocket("127.0.0.1", SimpleSSLServer.PORT)) {
                LOGGER.info("{} start handshake ...", socket);
                socket.startHandshake();
                try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    out.writeUTF("Hello World!");
                    out.flush();
                    try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                        LOGGER.info("Get String: {}", in.readUTF());
                    }
                }
            }
        }
    }

    private static SSLContext buildSSLContext(char[] password) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(classLoader.getResourceAsStream("client.truststore"), password);
            String algorithmName = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithmName);
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

//            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(classLoader.getResourceAsStream("client.keystore"), password);

            keyManagerFactory.init(keyStore, password);
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            sslContext.init(keyManagers, trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException
                | KeyManagementException | UnrecoverableKeyException e) {
            LOGGER.error("Can't initialize SSLContext", e);
            return null;
        }

    }
}
