package com.zizhizhan.interview.asm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DemoApp {

    private final Lock lock = new ReentrantLock();

    public synchronized String hello(String message) {
        return "Hello " + message + "!";
    }

    public String world(String message) {
        synchronized (this) {
            return "Hello " + message + "!";
        }
    }

    public int plus(int a, int b) {
        lock.lock();
        try {
            return a + b;
        } finally {
            lock.unlock();
        }
    }

}
