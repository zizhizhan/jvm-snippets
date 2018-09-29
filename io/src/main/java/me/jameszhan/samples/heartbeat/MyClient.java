package me.jameszhan.samples.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServer.class);

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("Localhost", 8868);

            LOGGER.info("SocketAddress: {} => {}, InetAddress: {} => {}, Port: {} => {}, BufferSize Send: {}, " +
                            "Received: {}, Linger: {}, Timeout: {}, trafficClass: {}",
                    socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),
                    socket.getLocalAddress(), socket.getInetAddress(),
                    socket.getLocalPort(), socket.getPort(),
                    socket.getSendBufferSize(),  socket.getReceiveBufferSize(),
                    socket.getSoLinger(), socket.getSoTimeout(),
                    socket.getTrafficClass());

            ObjectOutputStream sockout = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            ObjectInputStream sockin = new ObjectInputStream(new DataInputStream(socket.getInputStream()));

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();

            while (!line.equalsIgnoreCase("bye")) {
                sockout.writeObject(line);
                sockout.flush();
                sockout.writeObject(line + ".back");
                sockout.flush();
                System.out.println("server: " + sockin.readObject());
                line = br.readLine();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}