package me.jameszhan.pattern.promise.v0;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.*;

@Slf4j
public class PromiseV0Test {

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
        PromiseV0 p1 = new PromiseV0((resolve, reject) ->  resolve.accept(100));
        p1.then((o) -> Assert.assertEquals(100, o));
    }

    @Test
    public void test02() {
        PromiseV0 p1 = new PromiseV0((resolve, reject) -> {
            async(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.info("Unexpected Exception.", e);
                }
                resolve.accept(100);
            });
        });
        p1.then((o) -> Assert.assertEquals(100, o));
    }

    @Test
    public void test03() {
        PromiseV0 p1 = new PromiseV0((resolve, reject) ->  resolve.accept(100));
        PromiseV0 p2 = p1.then(System.out::println);
        PromiseV0 p3 = p2.then(System.out::println);
        PromiseV0 p4 = p3.then(System.out::println);
        PromiseV0 p5 = p4.then(System.out::println);

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);
    }


    private static void async(Runnable runnable) {
        executor.execute(runnable);
    }

}
