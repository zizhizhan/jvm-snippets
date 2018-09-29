package me.jameszhan.net;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM1:15
 */
public class TLSClient {

    public static void main(String[] args) {
        try {
            char[] password = "password".toCharArray();
            SSLContext context = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = TLSWebServer.createKeyManagerFactory("client.keystore", password);
            TrustManagerFactory trustManagerFactory = TLSWebServer.createTrustManagerFactory("client.truststore", password);
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

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

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
