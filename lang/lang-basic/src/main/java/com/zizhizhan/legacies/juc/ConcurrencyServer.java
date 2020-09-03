package com.zizhizhan.legacies.juc;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrencyServer {

    private final static AtomicBoolean s_initialized = new AtomicBoolean();
    private final static CountDownLatch s_latch = new CountDownLatch(1);

    private static ConcurrencyServer s_server;

    private final String name = UUID.randomUUID().toString();

    private String getName() {
        return name;
    }

    public static void main(String[] args) {
        Executor exec = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            exec.execute(() -> {
                try {
                    System.out.println(instance().name);
                    System.out.println(instance().name);
                    System.out.println(instance().name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static ConcurrencyServer instance() throws InterruptedException {
        if (s_initialized.compareAndSet(false, true)) {
            s_server = new ConcurrencyServer();
            Thread.sleep(1000);
            s_latch.countDown();
        }

        s_latch.await();
        return s_server;
    }


}
