package me.jameszhan.pattern.promise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.*;


/**
 * Promise.prototype.then(onFulfilled, onRejected);
 * 任何情况下 then 在执行过程中是立即返回，并总是返回一个新的promise2。
 * promise2的就绪，是由promise.then(onFulfilled, onRejected)中onFulfilled/onRejected的返回值所决定的
 *
 **/
@Slf4j
public class Promise<T> {

    private static final int PENDING = 1;
    private static final int FULFILLED = 2;
    private static final int REJECTED = 3;
    private static ExecutorService executor = ForkJoinPool.commonPool();

    private volatile int state = PENDING;
    private final Object lock = new Object();

    private Object value;

    public Promise(Callable<T> callable) {
        this((target, resolve, reject) -> {
            executor.execute(() -> {
                try {
                    resolve.accept(callable.call());
                } catch (Throwable t) {
                    reject.accept(t);
                }
            });
        });
    }

    public Promise(PromiseExecutor<T> executor) {
        if (executor != null) {
            try {
                executor.execute(this, this::resolveCallback, this::rejectCallback);
            } catch (Throwable t) {
                rejectCallback(t);
            }
        } else {
            this.state = FULFILLED;
        }
    }

    private void resolveCallback(T value) {
        this.fulfill(value);
    }

    private void rejectCallback(Throwable reason) {
        this.reject(reason);
    }

    public Promise<Void> thenAccept(Consumer<T> onFulfilled) {
        return then((o) -> {
            onFulfilled.accept(o);
            return null;
        });
    }

    public <R> Promise<R> then(Function<T, R> onFulfilled) {
        return then(onFulfilled, null);
    }

    public <R> Promise<R> then(Function<T, R> onFulfilled, Consumer<Throwable> onRejected) {
        return new Promise<>((target, resolve, reject) -> {
            try {
                target.resolveCallback(onFulfilled.apply(this.get()));
            } catch (Throwable t) {
                if (onRejected != null) {
                    onRejected.accept(t);
                } else {
                    log.error("No reject handler for.", t);
                }
                target.rejectCallback(t);
            }
        });
    }

    public T get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (state == PENDING) {
                lock.wait();
            }
        }
        if (state == FULFILLED) {
            return (T)value;
        } else {
            throw new ExecutionException((Throwable) value);
        }
    }

    private void fulfill(T value) {
        this.value = value;
        this.state = FULFILLED;
        log.info("{} fulfill {}.", this, value);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void reject(Throwable value) {
        this.value = value;
        this.state = REJECTED;
        log.info("{} reject {}.", this, value);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
