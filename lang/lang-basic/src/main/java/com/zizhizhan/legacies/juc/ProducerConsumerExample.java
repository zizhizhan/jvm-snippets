package com.zizhizhan.legacies.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class ProducerConsumerExample {
    private static Semaphore empty = new Semaphore(10);

    private static Semaphore full = new Semaphore(0);

    static CountDownLatch gate = new CountDownLatch(20);

    static CountDownLatch monitor = new CountDownLatch(10);

    private static AtomicInteger p = new AtomicInteger(0);

    private static AtomicInteger c = new AtomicInteger(0);

    static AtomicInteger pp = new AtomicInteger(0);

    static AtomicInteger cc = new AtomicInteger(0);

    static List<String> logs = new ArrayList<String>();

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            new Thread(new Producer("pro-" + i, gate)).start();
            gate.countDown();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Consumer("com-" + i, gate)).start();
            gate.countDown();
        }

        try {
            monitor.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Print info:");

        synchronized (logs) {
            for (String log : logs) {
                System.out.println(log);
            }
        }

    }

    static class Producer implements Runnable {

        private String name;

        private CountDownLatch latch;

        public Producer(String name, CountDownLatch latch) {
            super();
            this.name = name;
            this.latch = latch;
        }

        public void run() {

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (p.get() < 100) {
                synchronized (logs) {
                    if (empty.tryAcquire()) {
                        log(this.name + " produce " + p.incrementAndGet()
                                + " product, current permits: "
                                + empty.availablePermits());
                        full.release();
                    }
                }
                //Thread.yield();
            }

            monitor.countDown();

        }

    }

    static class Consumer implements Runnable {

        private String name;

        private CountDownLatch latch;

        public Consumer(String name, CountDownLatch latch) {
            super();
            this.name = name;
            this.latch = latch;
        }

        public void run() {

            try {
                latch.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while (c.get() < 100) {

                synchronized (logs) {
                    if (full.tryAcquire()) {
                        log(this.name + " consume " + c.incrementAndGet()
                                + " product. current permits: "
                                + full.availablePermits());
                        empty.release();
                    }
                }
                //Thread.yield();
            }

            monitor.countDown();
        }

    }

    public static void log(String log) {
        logs.add(log);
    }


}
