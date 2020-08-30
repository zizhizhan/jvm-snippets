package com.zizhizhan.juc.aqs;

public class AQSMain {

    private static class Sync extends AbstractQueuedSynchronizer {

    }

    public static void main(String[] args) {
        Sync sync = new Sync();
        Node node = new Node();
        sync.enq(node);
        sync.enq(new Node());
        System.out.println(sync.head);
        System.out.println(sync.tail);
        System.out.println(node);

        sync.addWaiter(Node.EXCLUSIVE);
        sync.addWaiter(Node.SHARED);
        sync.addWaiter(Node.EXCLUSIVE);

        System.out.println();
    }

}
