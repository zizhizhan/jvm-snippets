package com.zizhizhan.juc.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/5/11
 *         Time: 8:31 PM
 */
@Slf4j
public class DuTask extends RecursiveTask<Long> {

    private final Path path;

    public DuTask(String dir) {
        this.path = Paths.get(dir);
    }

    public DuTask(Path path) {
        this.path = path;
    }

    @Override
    protected Long compute() {
        long total = 0;
        List<DuTask> tasks = new ArrayList<>();
        log.debug("Compute path: {}.", path);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path subPath : ds) {
                if (Files.isDirectory(subPath, LinkOption.NOFOLLOW_LINKS)) {
                    tasks.add(new DuTask(subPath));
                } else {
                    total += Files.size(subPath);
                }
            }
            if (!tasks.isEmpty()) {
                for (DuTask task : invokeAll(tasks)) {
                    long size = task.join();
                    total += size;
                }
            }
        } catch (IOException e) {
            log.warn("Open {} error.", path, e);
            return 0L;
        }
        return total;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            long t1 = System.nanoTime();
            Long total = pool.invoke(new DuTask("/opt/rootfs/lts"));
            long t2 = System.nanoTime();
            System.out.format("Total: %d, time: %d\n", total, t2 - t1);
        } finally {
            pool.shutdown();
        }
    }
}
