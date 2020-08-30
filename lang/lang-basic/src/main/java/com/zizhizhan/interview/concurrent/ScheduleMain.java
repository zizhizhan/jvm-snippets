package com.zizhizhan.interview.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduleMain {

    public static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("beat.sender");
        return thread;
    });

    public static void main(String[] args) throws InterruptedException {
        executorService.schedule(new BeatTask(0), 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
    }

    private static class BeatTask implements Runnable {
        private final int counter;

        public BeatTask(int counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            log.info("Scheduled {}.", counter);
            executorService.schedule(new BeatTask(counter + 1), 1, TimeUnit.SECONDS);
        }
    }

}
