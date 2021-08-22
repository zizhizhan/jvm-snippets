package com.zizhizhan.juc;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/5/11
 *         Time: 11:54 PM
 */
@Slf4j
public class Du {
    public static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private static final BlockingQueue<File> queue = new LinkedBlockingQueue<>();
    private static final AtomicLong total = new AtomicLong(0);
    private static final CountDownLatch countDown = new CountDownLatch(2);
    private static final File NULL_FILE = new File("NULL_FILE");

    static void walkDirectory(File dir) {
        log.debug("Compute path: {}.", dir);
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    try {
                        queue.put(f);
                    } catch (InterruptedException e) {
                        log.warn("Unexpected error: ", e);
                    }
                } else if (f.isDirectory()){
                    walkDirectory(f);
                } else {
                    log.warn("Ignore unaccepted file: {}", f.getAbsolutePath());
                }
            }
        }
    }

    static class Producer implements Runnable {
        public final File dir;

        Producer(File dir) {
            this.dir = dir;
        }

        @Override public void run() {
            try {
                walkDirectory(dir);
                try {
                    queue.put(NULL_FILE);
                } catch (InterruptedException e) {
                    log.warn("Unexpected error: ", e);
                }
            } finally {
                countDown.countDown();
            }
        }
    }

    public static class Consumer implements Runnable {
        private static final AtomicBoolean finished = new AtomicBoolean(false);

        @Override public void run() {
            try {
                while (!finished.get()) {
                    try {
                        File file = queue.take();
                        if (file != NULL_FILE) {
                            total.addAndGet(file.length());
                        } else {
                            finished.set(true);
                        }
                    } catch (InterruptedException e) {
                        log.warn("Take interrupted: ", e);
                        break;
                    }
                }
            } finally {
                countDown.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService producers = new ScheduledThreadPoolExecutor(1);
        ExecutorService consumers = new ScheduledThreadPoolExecutor(CORE_SIZE);

        try {
            long t1 = System.nanoTime();
                producers.execute(new Producer(new File("/opt/rootfs/codes")));
            for (int i = 0; i < CORE_SIZE; i++) {
                consumers.execute(new Consumer());
            }

            countDown.await();

            long t2 = System.nanoTime();
            System.out.format("Total: %d, time: %d\n", total.longValue(), t2 - t1);
        } finally {
            producers.shutdown();
            consumers.shutdownNow();
        }
    }
}
