package me.jameszhan.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:32
 */
public class TcpClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8868);
        LOGGER.info("SocketAddress: {} => {}, InetAddress: {} => {}, Port: {} => {}, BufferSize Send: {}, " +
                        "Received: {}, Linger: {}, Timeout: {}, trafficClass: {}",
                socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),
                socket.getLocalAddress(), socket.getInetAddress(),
                socket.getLocalPort(), socket.getPort(),
                socket.getSendBufferSize(),  socket.getReceiveBufferSize(),
                socket.getSoLinger(), socket.getSoTimeout(),
                socket.getTrafficClass());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket
                .getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String str = wt.readLine();
            out.println(str);
            out.flush();
            if (str.equals("end")) {
                break;
            }
            System.out.println(in.readLine());
        }
    }
}
