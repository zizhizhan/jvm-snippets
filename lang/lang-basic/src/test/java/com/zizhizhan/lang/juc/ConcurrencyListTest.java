package com.zizhizhan.lang.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConcurrencyListTest {

    private static final int taskCount = 16;
    private static final ExecutorService executor = Executors.newFixedThreadPool(taskCount);

    public static void main(String[] args) throws Exception {
        List<Integer> list = new ThreadSafeList<>();
        // List<Integer> list = new LinkedList<>();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch taskLatch = new CountDownLatch(taskCount);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(() -> {
                taskLatch.countDown();
                try {
                    log.info("{} is waiting to start", Thread.currentThread().getName());
                    startLatch.await();
                    log.info("{} is running", Thread.currentThread().getName());

                    addRemoveItems(list);
                } catch (InterruptedException e) {
                    log.warn("Unexpected Error", e);
                }
            });
        }

        taskLatch.await();
        Thread.sleep(1000);
        startLatch.countDown();

        Thread.sleep(10000);
        log.info("List size is {}", list.size());
        executor.shutdown();
    }

    private static void addRemoveItems(List<Integer> list) {
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
    }


}
