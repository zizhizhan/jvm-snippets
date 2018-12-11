package me.jameszhan.pattern.promise.simple;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Promise<T> {

    private static ExecutorService executor = ForkJoinPool.commonPool();

    private static final int PENDING = 1;
    private static final int FULFILLED = 2;
    private static final int REJECTED = 3;

    private volatile int state = PENDING;
    private final Object lock = new Object();

    private Object value;

    private Promise() {}

    public static <V> Promise<V> async(Callable<V> task) {
        Promise<V> promise = new Promise<>();
        executor.execute(() -> {
            try {
                promise.resolveCallback(task.call());
            } catch (Throwable t) {
                promise.rejectCallback(t);
            }
        });
        return promise;
    }

    public static <V> Promise<V> resolve(V value) {
        Promise<V> promise = new Promise<>();
        promise.resolveCallback(value);
        return promise;
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
        Promise<T> source = this;
        Promise<R> target = new Promise<>();
        executor.execute(() -> {
            try {
                target.resolveCallback(onFulfilled.apply(source.get()));
            } catch (Throwable t) {
                if (onRejected != null) {
                    onRejected.accept(t);
                } else {
                    log.error("Unexpected Exception.", t);
                }
                target.rejectCallback(t.getCause());
            }
        });
        return target;
    }

    @SuppressWarnings("unchecked")
    private T get() throws InterruptedException, ExecutionException {
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

    private void reject(Throwable reason) {
        this.value = reason;
        this.state = REJECTED;
        log.info("{} reject {}.", this, value);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public String toString() {
        if (this.state == PENDING) {
            return "Promise{ <pending> }";
        } else if (this.state == FULFILLED) {
            return "Promise{ " + this.value + " }";
        } else {
            return "Promise{ <error> }";
        }
    }
}
