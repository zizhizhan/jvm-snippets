package me.jameszhan.io.net.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:58
 */
class ConnectionHandler implements Runnable {
    private Socket socket;
    private ISimpleSocketServerProtocol protocol;

    private InputStream in;
    private OutputStream out;

    /**
     * Will get the input and output stream from socket, and setup the reader
     * and writer.
     *
     * @param socket
     */
    public ConnectionHandler(Socket socket, ISimpleSocketServerProtocol protocol) {
        this.socket = socket;
        this.protocol = protocol;
    }

    /**
     * Continuously process input and output from the socket until a termination
     * code is sent.
     *
     */
    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            protocol.handle(in, out);
        } catch (IOException e) {
            System.err.println(e.toString());
        } finally {
            close();
        }
    }

    /**
     * Close the reader and writer first, than close off the socket.
     *
     */
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            System.err.println("Error while closing reader and writers: "
                    + ex.toString());
        } finally {
            in = null;
            out = null;
        }

        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Error closing socket: " + ex.toString());
        } finally {
            socket = null;
        }
    }
}
