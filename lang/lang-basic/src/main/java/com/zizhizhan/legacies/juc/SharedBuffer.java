package com.zizhizhan.legacies.juc;

import java.util.concurrent.Semaphore;


public class SharedBuffer<E> {

    private final E[] items;

    private Semaphore notFull;

    private Semaphore notEmpty;

    private int count = 0;

    @SuppressWarnings("unchecked")
    public SharedBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        this.items = (E[]) new Object[size];
        notFull = new Semaphore(size);
        notEmpty = new Semaphore(0);

    }

    public void put(E e) throws InterruptedException {
        notFull.acquire();
        synchronized (items) {
            items[count++] = e;
            log("put in: " + e);
            notEmpty.release();
        }

    }

    public boolean offer(E e) {
        if (notFull.tryAcquire()) {
            synchronized (items) {
                items[count++] = e;
                log("put in: " + e);
            }
            notEmpty.release();
            return true;
        } else {
            return false;
        }

    }

    public E poll() {

        if (notEmpty.tryAcquire()) {
            synchronized (items) {
                E e = items[--count];
                items[count] = null;
                log("take out: " + e);
                notFull.release();
                return e;
            }
        } else {
            return null;
        }

    }

    public E take() throws InterruptedException {

        notEmpty.acquire();
        synchronized (items) {
            E e = items[--count];
            items[count] = null;
            notFull.release();
            log("take out: " + e);
            return e;
        }

    }

    static void log(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }


}
