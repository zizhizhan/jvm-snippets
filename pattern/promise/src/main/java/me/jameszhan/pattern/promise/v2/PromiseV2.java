package me.jameszhan.pattern.promise.v2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class PromiseV2<T> {

    private volatile int state;
    private static final int PENDING     = 0;
    private static final int FULFILLED   = 1;
    private static final int REJECTED    = 2;
    private Object value;
    private final Object lock = new Object();

    public PromiseV2(PromiseExecutor<T> executor) {
        this.state = PENDING;
        try {
            log.info("{} executor start.", this);
            executor.execute(this);
        } catch (Throwable t) {
            reject(t);
        } finally {
            log.info("{} executor end.", this);
        }
    }

    public <U> PromiseV2<U> then(Function<T, U> onFulfilled) {
        return then(onFulfilled, null);
    }

    public <U> PromiseV2<U> then(Function<T, U> onFulfilled, Consumer<Throwable> onRejected) {
        return new PromiseV2<>((promise) -> {
            try {
                U transformValue = onFulfilled.apply(this.get());
                promise.fulfill(transformValue);
            } catch (Throwable t) {
                if (onRejected != null) {
                    onRejected.accept(t);
                    promise.reject(t);
                } else {
                    log.error("Promise reject.", t);
                }
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

    void fulfill(T value) {
        this.value = value;
        this.state = FULFILLED;
        log.info("{} fulfill {}.", this, value);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    void reject(Throwable value) {
        this.value = value;
        this.state = REJECTED;
        log.info("{} reject {}.", this, value);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public interface PromiseExecutor<T> {
        void execute(PromiseV2<T> promise);
    }
}
