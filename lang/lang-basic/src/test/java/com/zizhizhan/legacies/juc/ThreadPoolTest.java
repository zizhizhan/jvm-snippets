package com.zizhizhan.legacies.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolTest {

    private final ExecutorService threadPool = new ThreadPoolExecutor(1,
			Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>(true));

    private final Semaphore semaphore;

    public ThreadPoolTest(int maxConcurrency) {
        semaphore = new Semaphore(maxConcurrency);
    }

    public void execute(Runnable command) {
        acquireConcurrencyPermit();
        threadPool.execute(new Operation(command));
    }

    public void acquireConcurrencyPermit() {
        boolean havePermit = false;
        while (!havePermit) {
			if (!semaphore.tryAcquire()) {
				semaphore.acquireUninterruptibly();
			}
            havePermit = true;
        }
    }

    public void releaseConcurrencyPermit() {
        semaphore.release();
    }

    public static void test(int c) {
        ThreadPoolTest tp = new ThreadPoolTest(100);
        final AtomicLong max = new AtomicLong(0);

        Runnable r = () -> {
			String name = Thread.currentThread().getName();
			long id = Integer.parseInt(name.substring(name.lastIndexOf('-') + 1));

			if (max.get() < id) {
				max.set(id);
			}
		};

        for (int i = 0; i < c; i++) {
            tp.execute(r);
        }

        tp.shutdown();
        System.out.println(max.get());
    }

    public void shutdown() {
        threadPool.shutdown();
    }

    public class Operation implements Runnable {
        private final Runnable command;

        public Operation(Runnable command) {
            super();
            this.command = command;
        }

        public void run() {
            //System.out.println(Thread.currentThread().getName() + " begin!");
            try {
                command.run();
            } finally {
                releaseConcurrencyPermit();
                //System.out.println(Thread.currentThread().getName() + " release!");
            }
        }
    }

    public static void main(String[] args) {
        int c = 1;
        for (int i = 0; i < 8; i++) {
            c = c * 10;
            System.out.format("c=%s\n", c);
            test(c);
        }
    }

}
