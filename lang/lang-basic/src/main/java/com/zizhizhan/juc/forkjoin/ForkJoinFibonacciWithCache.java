package com.zizhizhan.juc.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/5/11
 *         Time: 9:39 PM
 */
@Slf4j
public class ForkJoinFibonacciWithCache extends RecursiveTask<Long> {
    private static final Map<Integer, Long> cache = new ConcurrentHashMap<>();
    private final int n;

    public ForkJoinFibonacciWithCache(int n) {
        this.n = n;
    }

    public Long compute() {
        if (n <= 1) {
            return (long) n;
        } else if (cache.containsKey(n)) {
            return cache.get(n);
        }
        ForkJoinFibonacciWithCache f1 = new ForkJoinFibonacciWithCache(n - 1);
        f1.fork();
        ForkJoinFibonacciWithCache f2 = new ForkJoinFibonacciWithCache(n - 2);
        Long result = f2.compute() + f1.join();
        log.info("Hit fibonacci({}) = {}", n, result);
        cache.put(n, result);
        return result;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println(pool.invoke(new ForkJoinFibonacciWithCache(50)));
    }
}
