package me.jameszhan.io.net.framework;

import me.jameszhan.io.net.select.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);

    private Socket socket;
    private IOProtocol protocol;

    /**
     * Will get the input and output stream from socket, and setup the reader
     * and writer.
     *
     * @param socket
     */
    public ConnectionHandler(Socket socket, IOProtocol protocol) {
        this.socket = socket;
        this.protocol = protocol;
    }

    /**
     * Continuously process input and output from the socket until a termination code is sent.
     *
     */
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            protocol.handle(in, out);
        } catch (IOException e) {
            LOGGER.error("Unexpected IO Error.", e);
        }
    }

    /**
     * Close the reader and writer first, than close off the socket.
     *
     */
    public void close() {
        IOUtils.close(socket);
        socket = null;
    }
}
