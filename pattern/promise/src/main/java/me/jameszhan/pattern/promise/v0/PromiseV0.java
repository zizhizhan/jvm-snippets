package me.jameszhan.pattern.promise.v0;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Slf4j
public class PromiseV0 {

    private volatile int state;
    private static final int PENDING     = 0;
    private static final int FULFILLED   = 1;
    private static final int REJECTED    = 2;
    private Object value;
    private final Object lock = new Object();

    public PromiseV0(PromiseExecutor executor) {
        this.state = PENDING;
        Consumer<Object> resolve = this::fulfill;
        Consumer<Throwable> reject = this::reject;
        try {
            log.info("{} executor start.", this);
            executor.execute(resolve, reject);
        } catch (Throwable t) {
            reject.accept(t);
        } finally {
            log.info("{} executor end.", this);
        }
    }

    public PromiseV0 then(Consumer<Object> onFulfilled) {
        return then(onFulfilled, null);
    }

    public PromiseV0 then(Consumer<Object> onFulfilled, Consumer<Throwable> onRejected) {
        return new PromiseV0(((resolve, reject) -> {
            try {
                Object value = this.get();
                onFulfilled.accept(value);
            } catch (Throwable t) {
                if (onRejected != null) {
                    onRejected.accept(t);
                } else {
                    log.error("Promise reject.", t);
                }
            }
        }));
    }

    public Object get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (state == PENDING) {
                lock.wait();
            }
        }
        if (state == FULFILLED) {
            return value;
        } else {
            throw new ExecutionException((Throwable) value);
        }
    }

    private void fulfill(Object value) {
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

    interface PromiseExecutor {

        void execute(Consumer<Object> resolve, Consumer<Throwable> reject);

    }



}
