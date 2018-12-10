package me.jameszhan.pattern.promise.v1;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class PromiseV1Test {

    private static ExecutorService executor;

    @BeforeClass
    public static void setupExecutor() {
        executor = Executors.newFixedThreadPool(10);
    }

    @AfterClass
    public static void teardownExecutor() {
        executor.shutdown();
    }

    @Test
    public void test01() {
        new PromiseV1<Integer>((promise, resolve, reject) ->  resolve.accept(100)).then(o -> {
            Assert.assertEquals(100, o.intValue());
            return 100 + 1;
        });
    }

    @Test
    public void test02() {
        new PromiseV1<Integer>((promise, resolve, reject) -> {
            async(() -> {
                sleep(3000);
                resolve.accept(100);
            });
        })
                .then(o -> o + 1)
                .then(o -> o + 2)
                .then(o -> o + 3)
                .then(o -> o + 4)
                .then(o -> o + 5)
                .then(o -> {
                    log.info("Current value is {}.", o);
                    return "done";
                })
                .then(s -> {
                    log.info("Current string is {}.", s);
                    return null;
                });
    }


    private static void async(Runnable runnable) {
        executor.execute(runnable);
    }

    private static void sleep(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            log.info("Unexpected Exception.", e);
        }
    }

}
