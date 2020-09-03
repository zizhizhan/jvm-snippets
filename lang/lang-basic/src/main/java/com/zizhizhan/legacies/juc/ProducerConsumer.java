package com.zizhizhan.legacies.juc;

import java.util.concurrent.atomic.AtomicInteger;


public class ProducerConsumer {

    public static AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) {
        SharedBuffer<String> buf = new SharedBuffer<String>(5);
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Consumer(buf));
            t.setName("cons-" + i);
            t.start();
        }

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Producer(buf));
            t.setName("prod-" + i);
            t.start();
        }

    }

    static class Consumer implements Runnable {

        private SharedBuffer<String> buffer;

        public Consumer(SharedBuffer<String> buffer) {
            super();
            this.buffer = buffer;
        }

        public void run() {
            for (int i = 0; i < 15; i++) {
                try {
                    buffer.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class Producer implements Runnable {

        private SharedBuffer<String> buffer;


        public Producer(SharedBuffer<String> buffer) {
            super();
            this.buffer = buffer;
        }

        public void run() {
            for (int i = 0; i < 15; i++) {
                try {
                    buffer.put(Thread.currentThread().getName() + " produce the " + i + "th products!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}





