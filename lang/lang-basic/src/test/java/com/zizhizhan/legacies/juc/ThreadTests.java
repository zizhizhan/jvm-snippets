package com.zizhizhan.legacies.juc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTests {

    public static void main(String[] args) {
        ThreadPoolExecutor exec = new ThreadPoolExecutor(0, 10, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        final AtomicInteger count = new AtomicInteger();

        for (int i = 0; i < 50; i++) {
            exec.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(count.incrementAndGet() + ": " + "10000");
            });
        }

        System.out.println("OK");
        exec.shutdown();
    }
}
