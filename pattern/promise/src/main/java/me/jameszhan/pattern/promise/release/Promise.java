package me.jameszhan.pattern.promise.release;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
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

    private volatile int state = PENDING;
    private final Object lock = new Object();
    private Runnable fulfillmentAction;
    private Object value;

    private Promise() {}

    public static <V> Promise<V> async(Callable<V> task, Executor executor) {
        Promise<V> promise = new Promise<>();
        executor.execute(() -> {
            try {
                promise.fulfill(task.call());
            } catch (Throwable t) {
                promise.reject(t);
            }
        });
        return promise;
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
        Promise<R> target = new Promise<>();
        this.fulfillmentAction = () -> {
            try {
                target.fulfill(onFulfilled.apply(this.get()));
            } catch (Throwable t) {
                if (onRejected != null) {
                    onRejected.accept(t);
                } else {
                    log.error("No reject handler for.", t);
                }
                target.reject(t);
            }
        };
        return target;
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
        if (this.fulfillmentAction != null) {
            this.fulfillmentAction.run();
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
