package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/10
 * Time: AM1:15
 */
public class SimpleTLSClient {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleTLSClient.class);
    private static final char[] PASSWORD = "password".toCharArray();

    private String host;
    private int port;

    public SimpleTLSClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String invoke(String request) throws Exception {
        SSLContext sslContext = buildSSLContext("client.keystore", "client.truststore", PASSWORD);
        SSLSocketFactory factory = sslContext.getSocketFactory();

        try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
            LOGGER.info("{} start handshake ...", socket);
            socket.startHandshake();
            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                out.writeUTF(request);
                out.flush();
                try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                    return in.readUTF();
                }
            }
        }
    }

    private static SSLContext buildSSLContext(String keyStoreFile, String trustStoreFile, char[] passphrase)
            throws GeneralSecurityException, IOException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = SimpleTLSServer.createKeyManagerFactory(keyStoreFile, passphrase);
        TrustManagerFactory trustManagerFactory = createTrustManagerFactory(trustStoreFile, passphrase);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }

    private static TrustManagerFactory createTrustManagerFactory(String trustStoreFile, char[] passphrase)
            throws GeneralSecurityException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(classLoader.getResourceAsStream(trustStoreFile), passphrase);
        trustManagerFactory.init(keyStore);
        return trustManagerFactory;
    }

}
