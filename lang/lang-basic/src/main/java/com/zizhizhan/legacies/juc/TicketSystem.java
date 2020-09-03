package com.zizhizhan.legacies.juc;

import java.util.concurrent.Semaphore;

public class TicketSystem {

    private final Semaphore semaphore = new Semaphore(100);
    private Integer tickets = 100;

    public void start(String name) {
        Thread t = new Thread(name) {
            public void run() {
                while (true) {
                    if (semaphore.tryAcquire()) {
                        int current;
                        synchronized (this) {
                            tickets--;
                            current = tickets;
                        }
                        System.out.println(Thread.currentThread().getName() + "已经卖出了第" + (100 - current) + "张票");
                    } else {
                        break;
                    }
                }
            }
        };
        t.start();
    }

    public static void main(String[] args) {
        TicketSystem ts = new TicketSystem();
        ts.start("james");
        ts.start("andy");
        ts.start("tom");
    }

}
