package me.jameszhan.io.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/9
 * Time: PM11:17
 */
public class ObjectServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectServer.class);

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8868);
        int num = 0;
        AtomicBoolean running = new AtomicBoolean(true);
        while (running.get()) {
            Socket socket = server.accept();
            LOGGER.info("\nSocketAddress: {} => {} \nInetAddress: {} => {} \nPort: {} => {} \nBufferSize Send: {} \n"
                            + "Received: {} \nLinger: {} \nTimeout: {} \ntrafficClass: {}",
                    socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(), socket.getLocalAddress(),
                    socket.getInetAddress(), socket.getLocalPort(), socket.getPort(), socket.getSendBufferSize(),
                    socket.getReceiveBufferSize(), socket.getSoLinger(), socket.getSoTimeout(), socket.getTrafficClass());

            run(socket, ++num);
        }
    }

    private static void run(Socket socket, int clientId) {
        new ServiceThread(socket, clientId).start();
    }

    private static class ServiceThread extends Thread {
        private Socket socket;
        private int clientNum;

        private ServiceThread(Socket socket, int clientNum) {
            this.socket = socket;
            this.clientNum = clientNum;
        }

        private void execute() throws Exception {
            try {
                try (
                        ObjectInputStream in = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
                        ObjectOutputStream out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
                ) {
                    while (true) {
                        try {
                            Object obj = in.readObject();
                            LOGGER.info("Client_{}: get {}.", clientNum, obj);
                            out.writeObject(obj);
                            out.flush();
                        } catch (EOFException e) {
                            break;
                        }
                    }
                }
            } finally {
                socket.close();
            }
        }

        public void run() {
            try {
                execute();
            } catch (Exception e) {
                LOGGER.error("Unexpected Error.", e);
            }
        }

    }


}
