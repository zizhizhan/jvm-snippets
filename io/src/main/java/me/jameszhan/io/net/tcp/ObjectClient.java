package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ObjectClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectServer.class);

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("Localhost", 8868);
        LOGGER.info("\nSocketAddress: {} => {} \nInetAddress: {} => {} \nPort: {} => {} \nBufferSize Send: {} \n"
                        + "Received: {} \nLinger: {} \nTimeout: {} \ntrafficClass: {}",
                socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),  socket.getLocalAddress(),
                socket.getInetAddress(), socket.getLocalPort(), socket.getPort(), socket.getSendBufferSize(),
                socket.getReceiveBufferSize(), socket.getSoLinger(), socket.getSoTimeout(), socket.getTrafficClass());

        ObjectOutputStream out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
        ObjectInputStream in = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();

            while (!"bye".equalsIgnoreCase(line)) {
                out.writeObject(line);
                out.flush();
                LOGGER.info("Server: {}", in.readObject());
                line = br.readLine();
            }
        } finally {
            in.close();
            out.close();
            socket.close();
        }
    }

}