package com.zizhizhan.juc;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/5/11
 *         Time: 8:14 PM
 */
@Slf4j
public class Du2 {

    public static class Pair<A, B> {
        public final A head;
        public final B succ;

        public Pair(A head, B succ) {
            this.head = head;
            this.succ = succ;
        }
    }

    public static long compute(File dir){
        if (dir.isFile()) {
            return dir.length();
        } else if (dir.isDirectory()){
            log.debug("Compute path: {}.", dir);
            long total = 0;
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    total += compute(f);
                }
            }
            return total;
        } else {
            log.warn("Ignore unaccepted file: {}", dir.getAbsolutePath());
            return 0L;
        }
    }

    private static Pair<Long, List<File>> computeLevel1(File dir){
        long total = 0;
        List<File> subDirs = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isDirectory()) {
                    subDirs.add(f);
                } else if (f.isFile()){
                    total += f.length();
                } else {
                    log.warn("Ignore unaccepted file: {}", dir.getAbsolutePath());
                }
            }
        }
        return new Pair<>(total, subDirs);
    }

    public static long asyncCompute(File file) throws InterruptedException, ExecutionException, TimeoutException {
        long total = 0;
        final ExecutorService pool = Executors.newFixedThreadPool(25);
        try {
            List<File> dirs = new ArrayList<>();
            dirs.add(file);
            while (!dirs.isEmpty()) {
                List<Future<Pair<Long, List<File>>>> futures = new ArrayList<>();
                for (final File f : dirs) {
                    futures.add(pool.submit(() -> computeLevel1(f)));
                }
                dirs.clear();
                for (Future<Pair<Long, List<File>>> future : futures) {
                    Pair<Long, List<File>> pair = future.get(30, TimeUnit.SECONDS);
                    dirs.addAll(pair.succ);
                    total += pair.head;
                }
            }
        } finally {
            pool.shutdown();
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        long t1 = System.nanoTime();
        long total =  compute(new File("/opt/rootfs/codes"));
        long t2 = System.nanoTime();
        System.out.format("Total: %d, time: %d\n", total, t2 - t1);

        t1 = System.nanoTime();
        total =  asyncCompute(new File("/opt/rootfs/codes"));
        t2 = System.nanoTime();
        System.out.format("Total: %d, time: %d\n", total, t2 - t1);
    }

}
