package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ObjectClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectServer.class);
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("Localhost", 8868);
        LOGGER.info("\nSocketAddress: {} => {} \nInetAddress: {} => {} \nPort: {} => {} \nBufferSize Send: {} \n"
                        + "Received: {} \nLinger: {} \nTimeout: {} \ntrafficClass: {}",
                socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),  socket.getLocalAddress(),
                socket.getInetAddress(), socket.getLocalPort(), socket.getPort(), socket.getSendBufferSize(),
                socket.getReceiveBufferSize(), socket.getSoLinger(), socket.getSoTimeout(), socket.getTrafficClass());
        try {
            executor.execute(() -> {
                try (ObjectInputStream in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))) {
                    while (true) {
                        try {
                            Object obj = in.readObject();
                            LOGGER.info("Server: {}.", obj);
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("Unknown Object.", e);
                        } catch (EOFException e) {
                            break;
                        } catch (IOException e) {
                            LOGGER.error("Unexpected Exception.", e);
                            break;
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("Unexpected Exception.", e);
                }
            });

            try (ObjectOutputStream out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String line = br.readLine();
                while (!"bye".equalsIgnoreCase(line)) {
                    out.writeObject(line);
                    out.flush();
                    line = br.readLine();
                }
            }
        } finally {
            socket.close();
        }
    }

}