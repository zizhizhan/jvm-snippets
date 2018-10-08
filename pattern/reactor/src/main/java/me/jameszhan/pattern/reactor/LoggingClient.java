package me.jameszhan.pattern.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 上午1:20
 */
public class LoggingClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingClient.class);
    private final ExecutorService service = Executors.newFixedThreadPool(4);

    /**
     * App client entry.
     *
     * @throws IOException if any I/O error occurs.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        LoggingClient appClient = new LoggingClient();
        appClient.start();
    }

    /**
     * Starts the logging clients.
     *
     * @throws IOException if any I/O error occurs.
     */
    public void start() throws IOException, InterruptedException {
        LOGGER.info("Starting logging clients");
        CountDownLatch latch = new CountDownLatch(4);
        service.execute(new TcpLoggingClient("Client01", 8886, latch));
        service.execute(new TcpLoggingClient("Client02", 8887, latch));
        service.execute(new UdpLoggingClient("Client03", 8888, latch));
        service.execute(new UdpLoggingClient("Client04", 8888, latch));
        latch.await();
        service.shutdown();
        service.awaitTermination(2, TimeUnit.SECONDS);
    }

    /**
     * Stops logging clients. This is a blocking call.
     */
    public void stop() {
        service.shutdown();
        if (!service.isTerminated()) {
            service.shutdownNow();
            try {
                service.awaitTermination(1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("exception awaiting termination", e);
            }
        }
        LOGGER.info("Logging clients stopped");
    }

    private static void artificialDelayOf(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep interrupted", e);
        }
    }

    /**
     * A logging client that sends requests to Reactor on TCP socket.
     */
    static class TcpLoggingClient implements Runnable {
        private final int serverPort;
        private final String clientName;
        private final CountDownLatch latch;

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
            for (int i = 0; i < 4; i++) {
                writer.println(clientName + "-request-" + i);
                writer.flush();

                byte[] data = new byte[1024];
                int read = inputStream.read(data, 0, data.length);
                if (read == 0) {
                    LOGGER.info("Read zero bytes");
                } else {
                    LOGGER.info("{} received {}", clientName, new String(data, 0, read));
                }

                artificialDelayOf(100);
            }
        }

    }

    /**
     * A logging client that sends requests to Reactor on UDP socket.
     */
    static class UdpLoggingClient implements Runnable {
        private final String clientName;
        private final InetSocketAddress remoteAddress;
        private final CountDownLatch latch;

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
                for (int i = 0; i < 4; i++) {
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
                    artificialDelayOf(100);
                }
            } catch (IOException e1) {
                LOGGER.error("error sending packets", e1);
            } finally {
                latch.countDown();
            }
        }
    }
}
