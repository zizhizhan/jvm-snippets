package me.jameszhan.pattern.promise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;


/**
 * Promise.prototype.then(onFulfilled, onRejected);
 * 任何情况下 then 在执行过程中是立即返回，并总是返回一个新的promise2。
 * promise2的就绪，是由promise.then(onFulfilled, onRejected)中onFulfilled/onRejected的返回值所决定的
 *
 **/
@Slf4j
public class Promise {

    private static final int RUNNING = 1;
    private static final int FULFILLED = 2;
    private static final int REJECTED = 3;

    private static Executor threadPool = ForkJoinPool.commonPool();
    private volatile int state = RUNNING;

    private final Object lock;

    private Object value;
    private Runnable fulfillmentAction;
    private Consumer<Promise> executable;


    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        new Promise(p -> {
            p.resolve(100);
        }).then(v -> {
            System.out.println(v);
            latch.countDown();
            return (Integer) v + 1;
        }).runAsync();

        latch.await();
    }

    private Promise(Consumer<Promise> executable) {
        this.lock = new Object();
        this.executable = executable;
    }

    public void runAsync() {
        threadPool.execute(() -> {
            try {
                log.info("{} begin runAsync {} with state {}.", this, this.value, state);
                this.executable.accept(this);
                log.info("{} finish runAsync {} with state {}.", this, this.value, state);
            } catch (Throwable t) {
                this.reject(t);
            }
        });
    }

    public <T, R> Promise then(Function<T, R> onFulfilled) {
        return new Promise(source -> {
            try {
                this.resolve(onFulfilled.apply(source.get()));
            } catch (Throwable throwable) {
                this.reject(throwable);
            }
        });
    }

    private void resolve(Object value) {
        this.value = value;
        this.state = FULFILLED;
        log.info("{} Resolve {} with state {}.", this, this.value, state);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void reject(Throwable t) {
        this.value = t;
        this.state = REJECTED;
        log.info("{} Reject {} with state {}.", this, this.value, state);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (state == RUNNING) {
                log.info("{} Get {} with state {}.", this, this.value, state);
                lock.wait();
            }
        }
        if (state == FULFILLED) {
            return (T) value;
        } else {
            throw new ExecutionException((Throwable) value);
        }
    }

//    public static <U> Promise runAsync(Supplier<U> supplier) {
//        Promise promise = new Promise();
//        promise.fulfillmentAction = () ->  threadPool.execute(promise.fulfillmentAction);
//        return promise;
//    }
//
//
//    private class ThenAction<T, V> implements Runnable {
//
//        private final Promise src;
//        private final Promise dest;
//        private final Function<? super T, V> func;
//
//        private ThenAction(Promise src, Promise dest, Function<? super T, V> func) {
//            this.src = src;
//            this.dest = dest;
//            this.func = func;
//        }
//
//        @Override
//        public void run() {
//            try {
//                dest.resolve(func.apply(src.get()));
//            } catch (Throwable throwable) {
//                dest.reject(throwable.getCause());
//            }
//        }
//    }

}
