package me.jameszhan.pattern.reactor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.CountDownLatch;

/**
 * A logging client that sends requests to Reactor on UDP socket.
 */
class UdpLoggingClient implements LoggingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpLoggingClient.class);
    private final String clientName;
    private final InetSocketAddress remoteAddress;
    private final CountDownLatch latch;
    private DatagramSocket datagramSocket;

    /**
     * Creates a new UDP logging client.
     *
     * @param clientName the name of the client to be sent in logging requests.
     * @param port the port on which client will send logging requests.
     * @throws UnknownHostException if localhost is unknown
     */
    public UdpLoggingClient(String clientName, int port, CountDownLatch latch) throws UnknownHostException {
        this.clientName = clientName;
        this.remoteAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
        this.latch = latch;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            this.datagramSocket = socket;
            for (int i = 0; i < 5; i++) {
                String message = clientName + "-request-" + i;
                DatagramPacket request = new DatagramPacket(message.getBytes(), message.getBytes().length, remoteAddress);
                socket.send(request);

                byte[] data = new byte[1024];
                DatagramPacket reply = new DatagramPacket(data, data.length);
                socket.receive(reply);
                if (reply.getLength() == 0) {
                    LOGGER.info("Read zero bytes");
                } else {
                    LOGGER.info("{} received {}", clientName, new String(reply.getData(), 0, reply.getLength()));
                }
                ReactorClient.artificialDelayOf(100);
            }
        } catch (IOException e1) {
            LOGGER.error("error sending packets", e1);
        } finally {
            latch.countDown();
        }
    }

    @Override
    public void close() {
        LOGGER.info("Close datagramSocket ({} -> {}).", datagramSocket.getLocalSocketAddress(),
                datagramSocket.getRemoteSocketAddress());
        datagramSocket.close();
    }
}
