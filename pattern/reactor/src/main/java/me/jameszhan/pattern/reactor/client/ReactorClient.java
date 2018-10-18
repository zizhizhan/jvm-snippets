package me.jameszhan.pattern.reactor.client;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/18
 * Time: 上午11:13
 */
public class ReactorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorClient.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final List<LoggingClient> loggingClients = Lists.newArrayList();

    /**
     * App client entry.
     *
     * @throws IOException if any I/O error occurs.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ReactorClient appClient = new ReactorClient();

        new Thread(appClient::stop).start();

        appClient.start();
    }

    /**
     * Starts the logging clients.
     *
     * @throws IOException if any I/O error occurs.
     */
    public void start() throws IOException, InterruptedException {
        LOGGER.info("Start launch LoggingClients...");
        CountDownLatch latch = new CountDownLatch(4);

        loggingClients.add(new TcpLoggingClient("tcp01", 8886, latch));
        loggingClients.add(new TcpLoggingClient("tcp02", 8887, latch));
        loggingClients.add(new UdpLoggingClient("udp01", 8888, latch));
        loggingClients.add(new UdpLoggingClient("udp02", 8889, latch));

        loggingClients.forEach(this.executor::execute);

        LOGGER.info("Finish launch LoggingClients...");
        latch.await();
        LOGGER.info("LoggingClients completed");
    }

    /**
     * Stops logging clients. This is a blocking call.
     */
    public void stop() {
        LOGGER.info("Start stop LoggingClients...");
        try {
            Thread.sleep(10000);
            executor.shutdown();
            loggingClients.forEach(LoggingClient::close);

            if (!executor.isTerminated()) {
                executor.shutdownNow();
                executor.awaitTermination(1000, TimeUnit.SECONDS);
            }

        } catch (InterruptedException e) {
            LOGGER.error("exception awaiting termination", e);
        }
        LOGGER.info("Logging clients stopped");
    }

    static void artificialDelayOf(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep interrupted", e);
        }
    }

}
