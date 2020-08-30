package com.zizhizhan.lang.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinFibonacci extends RecursiveTask<Long> {

    private final long n;

    public ForkJoinFibonacci(long n) {
        this.n = n;
    }

    @Override
    protected Long compute() {
        if (n <= 1) {
            return n;
        } else {
            ForkJoinFibonacci f1 = new ForkJoinFibonacci(n - 1);
            f1.fork();
            ForkJoinFibonacci f2 = new ForkJoinFibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }

    public static void main(String[] args) throws Exception {
        ForkJoinFibonacci fibonacci = new ForkJoinFibonacci(38);
        ForkJoinPool.commonPool().execute(fibonacci);
        System.out.println(fibonacci.get());
    }
}
