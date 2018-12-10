package me.jameszhan.pattern.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class PromiseTest {

    public static Integer getValueTask() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("interrupt ", e);
        }
        return 666;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        RunnablePromise<Integer> promise = new RunnablePromise<>(PromiseTest::getValueTask);
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                try {
                    log.info("Get result {}.", promise.get());
                } catch (InterruptedException e) {
                    log.error("Interrupt", e);
                } catch (ExecutionException e) {
                    log.error("Execution error.", e);
                }
            });
        }

        promise.run();

        executor.shutdown();
    }



}
