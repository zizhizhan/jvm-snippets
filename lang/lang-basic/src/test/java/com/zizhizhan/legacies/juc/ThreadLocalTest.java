package com.zizhizhan.legacies.juc;

public class ThreadLocalTest {

    private static int nextSerialNum = 0;
    private static Object lock = new Object();

    private static ThreadLocal<Integer> serialNum = new ThreadLocal<Integer>() {
        @Override
        protected synchronized Integer initialValue() {
            return new Integer(nextSerialNum++);
        }
    };

    public static int get() {
        return serialNum.get().intValue();
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Math.round(Math.random()) * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock) {
                        System.out.println(Thread.currentThread().getName() + ": " + ThreadLocalTest.get());
                    }
                }

                ;
            };
            t.start();
        }
        for (int i = 0; i < 5; i++) {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + ": " + ThreadLocalTest.get());
            }
        }
    }
}
