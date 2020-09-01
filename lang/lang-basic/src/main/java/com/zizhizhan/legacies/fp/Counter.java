package com.zizhizhan.legacies.fp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Counter {

	public static Closure<Void, Integer> counter(int initialValue) {
		final int[] holder = new int[] { initialValue };
		return (t) -> {
			synchronized (holder) {
				return ++holder[0];
			}
		};
	}	

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newFixedThreadPool(20);
		final Closure<Void, Integer> counter = counter(0);
		int size = 1000000;
		final CountDownLatch latch = new CountDownLatch(size);
		for (int i = 0; i < size; i++) {
			exec.execute(new Runnable() {
				public void run() {
					counter.f(null);
					latch.countDown();
				}
			});
		}
		exec.shutdown();
		latch.await();
		System.out.println("Result: " + counter.f(null));
	}

}
