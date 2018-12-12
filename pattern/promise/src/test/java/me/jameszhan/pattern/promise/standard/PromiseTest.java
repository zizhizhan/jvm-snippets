package me.jameszhan.pattern.promise.standard;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class PromiseTest {

    @Test
    public void tick() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Promise<Integer> p1 = new Promise<>(() -> sleepAndReturn(10, 3000));
        log.info("p1 = {}.", p1);
        Promise<Integer> p2 = p1.then((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p2 = {}.", p2);
        Promise<Integer> p3 = p2.then((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p3 = {}.", p3);
        Promise<Integer> p4 = p3.then((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p4 = {}.", p4);
        Promise<Integer> p5 = p4.then((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p5 = {}.", p5);
        Promise<Integer> p6 = p5.then((i) -> sleepAndReturn(i + 1, 1000));
        log.info("p6 = {}.", p6);
        p6.thenAccept((o) -> {
            log.info("target value is {}.", o);
            latch.countDown();
        });
        latch.await();
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
