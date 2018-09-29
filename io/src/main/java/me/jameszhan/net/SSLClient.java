package me.jameszhan.net;

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
public class SSLClient {

    public static void main(String[] args) {
        try {
            SSLContext context;
            KeyManagerFactory keyManagerFactory;
            KeyStore keyStore;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            char[] password = "password".toCharArray();

            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(classLoader.getResourceAsStream("client.truststore"), password);
            String algorithmName = TrustManagerFactory.getDefaultAlgorithm();
            System.out.println(algorithmName);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithmName);
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            context = SSLContext.getInstance("TLS");
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(classLoader.getResourceAsStream("client.keystore"), password);

            keyManagerFactory.init(keyStore, password);
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            context.init(keyManagers, trustManagers, null);

            SSLSocketFactory factory = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 8086);

            System.out.println("Starting handshake ... ");
            socket.startHandshake();

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello World! ... ");
            System.out.println("Wrote string ... ");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println("Get String: " + in.readUTF());

            socket.close();
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}
