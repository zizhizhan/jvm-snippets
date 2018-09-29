package me.jameszhan.net;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM1:21
 */
public class SSLServer {

    public static void main(String[] args) {
        try {
            SSLContext context;
            KeyManagerFactory keyManagerFactory;
            KeyStore keyStore;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            char[] passphrase = "password".toCharArray();
            context = SSLContext.getInstance("TLS");
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyStore = KeyStore.getInstance("JKS");

            keyStore.load(classLoader.getResourceAsStream("server.keystore"), passphrase);
            keyManagerFactory.init(keyStore, passphrase);

            context.init(keyManagerFactory.getKeyManagers(), null, null);
            ServerSocketFactory ssf = context.getServerSocketFactory();
            ServerSocket ss = ssf.createServerSocket(8086);
            System.out.println("Secure socket created. Listening ... ");

            Socket socket = ss.accept();

            System.out.println("Accepting-" + socket);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String get = in.readUTF();
            System.out.println("Read : " + get);
            out.writeUTF("Echo: " + get);

            socket.close();

        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException | KeyManagementException | UnrecoverableKeyException  e) {
            e.printStackTrace();
        }
    }

}
