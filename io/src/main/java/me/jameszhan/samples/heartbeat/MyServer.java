package me.jameszhan.samples.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:17
 */
public class MyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServer.class);

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8868);
        int num = 0;

        for (;;) {
            Socket socket = server.accept();
            LOGGER.info("SocketAddress: {} => {}, InetAddress: {} => {}, Port: {} => {}, BufferSize Send: {}, " +
                            "Received: {}, Linger: {}, Timeout: {}, trafficClass: {}",
                    socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),
                    socket.getLocalAddress(), socket.getInetAddress(),
                    socket.getLocalPort(), socket.getPort(),
                    socket.getSendBufferSize(),  socket.getReceiveBufferSize(),
                    socket.getSoLinger(), socket.getSoTimeout(),
                    socket.getTrafficClass());
            new ServiceThread(socket, ++num).start();
        }
    }

    static class ServiceThread extends Thread {
        private Socket socket;
        private int clientNum;
        private boolean shutdown;

        public ServiceThread(Socket socket, int clientNum){
            this.socket = socket;
            this.clientNum = clientNum;
            this.shutdown = false;
        }

        public boolean shutdown() {
            return shutdown;
        }

        public void run() {
            try {
                ObjectInputStream sockin = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
                ObjectOutputStream sockout = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));

                Object o;
                while((o = sockin.readObject()) != null){
                    System.out.println("Client" + clientNum + ": " + o);
                    sockout.writeObject(o);
                    sockout.flush();
                }

                sockin.close();
                sockout.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }


}
