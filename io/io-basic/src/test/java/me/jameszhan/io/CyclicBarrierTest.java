package me.jameszhan.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierTest.class);
    private static final int NUM_OF_COPY = 5;
    private static final CountDownLatch LATCH = new CountDownLatch(NUM_OF_COPY);
    private static CyclicBarrier barrier = new CyclicBarrier(NUM_OF_COPY,
            () -> LOGGER.info("{} Copy Threads Started.", NUM_OF_COPY));
    private static ExecutorService threadPool = Executors.newFixedThreadPool(6);

    @Test
    public void copyFiles() throws Exception {
        String source = "/etc/hosts";
        for (int i = 0; i < NUM_OF_COPY; i++) {
            String target = "/tmp/" + (i + 1) + ".txt";
            LOGGER.info("Begin copy {} to {}.", source, target);
            copyFile(source, target);
        }
        LATCH.await();
        threadPool.shutdown();
    }

    private static void copyFile(String source, String target) {
        threadPool.execute(() -> {
            try {
                barrier.await();
                doCopyFile(source, target);
                LOGGER.info("Copy {} to {} success.", source, target);
                LATCH.countDown();
            } catch (Exception e) {
                LOGGER.error("copy {} to {} with unexpected error.", e);
            }
        });
    }

    private static void doCopyFile(String source, String target) throws IOException {
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(target).getChannel()) {
            in.transferTo(0, in.size(), out);
        }
    }
}
