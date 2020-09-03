package com.zizhizhan.legacies.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

public class FutureTaskTests {
    public static void main(String[] args) {
        final Thread t = Thread.currentThread();

        final FutureTask<String> task = new FutureTask<>(() -> {
			int i = 0;
			while (i++ < 20) {
				try {
					System.out.println("Sleep.");
					Thread.sleep(1000);
					LockSupport.unpark(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "Hello World!";
		});

        new Thread() {
            public void run() {
                task.run();
            }
        }.start();

        LockSupport.park();

        task.cancel(true);
        task.cancel(true);
        task.cancel(true);

        System.out.println(task.isCancelled());
    }

}
