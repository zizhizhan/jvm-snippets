package me.jameszhan.pattern.reactor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * A logging client that sends requests to Reactor on TCP socket.
 */
class TcpLoggingClient implements LoggingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpLoggingClient.class);
    private final int serverPort;
    private final String clientName;
    private final CountDownLatch latch;
    private Socket socket;

    /**
     * Creates a new TCP logging client.
     *
     * @param clientName the name of the client to be sent in logging requests.
     * @param serverPort the port on which client will send logging requests.
     */
    public TcpLoggingClient(String clientName, int serverPort, CountDownLatch latch) {
        this.clientName = clientName;
        this.serverPort = serverPort;
        this.latch = latch;
    }

    public void run() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), serverPort)) {
            this.socket = socket;
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            sendLogRequests(writer, socket.getInputStream());
        } catch (IOException e) {
            LOGGER.error("error sending requests", e);
            throw new RuntimeException(e);
        } finally {
            latch.countDown();
        }
    }

    private void sendLogRequests(PrintWriter writer, InputStream inputStream) throws IOException {
        for (int i = 0; i < 5; i++) {
            writer.print(clientName + "-request-" + i);
            writer.flush();
            byte[] data = new byte[1024];
            int read = inputStream.read(data, 0, data.length);
            if (read == 0) {
                LOGGER.info("Read zero bytes");
            } else {
                LOGGER.info("{} received {}", clientName, new String(data, 0, read));
            }

            ReactorClient.artificialDelayOf(100);
        }
    }

    @Override
    public void close() {
        try {
            LOGGER.info("Close socket {}.", socket);
            socket.close();
        } catch (Exception e) {
            LOGGER.error("Error when close the socket {}.", socket, e);
        }
    }
}
