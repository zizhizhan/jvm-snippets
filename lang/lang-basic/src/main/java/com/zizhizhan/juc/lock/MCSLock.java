package com.zizhizhan.juc.lock;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MCSLock {
    private final AtomicReference<Node> tail;
    private final ThreadLocal<Node> myNode;

    public MCSLock() {
        tail = new AtomicReference<>();
        myNode = ThreadLocal.withInitial(Node::new);
    }

    public void lock() {
        Node node = myNode.get();
        Node pred = tail.getAndSet(node);
        if (pred != null) {
            node.locked = true;
            pred.next = node;
            while (node.locked) {
                log.debug("MCSLock try lock ({} -> {}).", pred, node);
            }
        }
    }

    public void unlock() {
        Node node = myNode.get();
        if (node.next == null) {
            if (tail.compareAndSet(node, null)) {
                return;
            }
            while (node.next == null) {
                log.debug("MCSLock try unlock {}.", node);
            }
        }
        node.next.locked = false;
        node.next = null;
    }

    @ToString
    static class Node {
        volatile boolean locked = false;
        Node next = null;
    }

    public static void main(String[] args) {
        MCSLock lock = new MCSLock();

        Runnable task = new Runnable() {
            private int a;

            @Override
            public void run() {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    a++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.warn("Invalid interrupt.", e);
                    }
                }
                System.out.println(a);
                lock.unlock();
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }
}
