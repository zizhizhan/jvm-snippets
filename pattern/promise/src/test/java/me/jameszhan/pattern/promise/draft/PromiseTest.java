package me.jameszhan.pattern.promise.draft;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class PromiseTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Test
    public void tick() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Promise<Integer> p1 = Promise.await(() -> sleepAndReturn(10, 3000), executor);
        log.info("p1 = {}.", p1);
        Promise<Integer> p2 = p1.thenApply((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p2 = {}.", p2);
        Promise<Integer> p3 = p2.thenApply((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p3 = {}.", p3);
        Promise<Integer> p4 = p3.thenApply((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p4 = {}.", p4);
        Promise<Integer> p5 = p4.thenApply((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p5 = {}.", p5);
        Promise<Integer> p6 = p5.thenApply((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p6 = {}.", p6);
        p6.thenAccept((o) -> {
            log.info("target value is {}.", o);
            latch.countDown();
        });
        latch.await();
        executor.shutdown();
    }

    private static int sleepAndReturn(int value, long timeMs) {
        try {
            log.info("current value is {}.", value);
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            log.error("Unexpected Exception.", e);
        }
        return value;
    }

}
