/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.jameszhan.pattern.promise.draft;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Promise represents a proxy for a value not necessarily known when the promise is created. It
 * allows you to associate dependent promises to an asynchronous action's eventual success value or
 * failure reason. This lets asynchronous methods return values like synchronous methods: instead 
 * of the final value, the asynchronous method returns a promise of having a value at some point 
 * in the future.
 *
 * @param <T> type of result.
 */
@Slf4j
public class Promise<T> implements Future<T> {

    private static final int RUNNING = 1;
    private static final int FAILED = 2;
    private static final int COMPLETED = 3;

    private volatile int state = RUNNING;
    private T value;
    private Throwable exception;

    private final Object lock = new Object();

    private Runnable fulfillmentAction;
    private Consumer<? super Throwable> exceptionHandler;

    /**
     * Creates a promise that will be fulfilled in future.
     */
    private Promise() {}

    /**
     * Executes the task using the executor in other thread and fulfills the promise returned
     * once the task completes either successfully or with an exception.
     *
     * @param task the task that will provide the value to fulfill the promise.
     * @param executor the executor in which the task should be run.
     * @return a promise that represents the result of running the task provided.
     */
    public static <V> Promise<V> await(final Callable<V> task, Executor executor) {
        Promise<V> promise = new Promise<>();
        executor.execute(() -> {
            try {
                promise.fulfill(task.call());
            } catch (Exception ex) {
                promise.reject(ex);
            }
        });
        return promise;
    }

    /**
     * Returns a new promise that, when this promise is fulfilled normally, is fulfilled with
     * result of this promise as argument to the action provided.
     * @param onFulfilled action to be executed.
     * @return a new promise.
     */
    public Promise<Void> thenAccept(Consumer<? super T> onFulfilled) {
        Promise<Void> target = new Promise<>();
        fulfillmentAction = new ConsumeAction(this, target, onFulfilled);
        return target;
    }

    /**
     * Returns a new promise that, when this promise is fulfilled normally, is fulfilled with
     * result of this promise as argument to the function provided.
     * @param onFulfilled function to be executed.
     * @return a new promise.
     */
    public <V> Promise<V> thenApply(Function<? super T, V> onFulfilled) {
        Promise<V> target = new Promise<>();
        fulfillmentAction = new TransformAction<V>(this, target, onFulfilled);
        return target;
    }

    /**
     * Set the exception handler on this promise.
     * @param onRejected a consumer that will handle the exception occurred while fulfilling the promise.
     * @return this
     */
    public Promise<T> caught(Consumer<? super Throwable> onRejected) {
        this.exceptionHandler = onRejected;
        return this;
    }

    /**
     * Fulfills the promise with the provided value.
     * @param value the fulfilled value that can be accessed using {@link #get()}.
     */
    private void fulfill(T value) {
        this.value = value;
        this.state = COMPLETED;
        synchronized (lock) {
            lock.notifyAll();
        }
        postFulfillment();
    }

    /**
     * Fulfills the promise with exception due to error in execution.
     * @param exception the exception will be wrapped in {@link ExecutionException}
     *        when accessing the value using {@link #get()}.
     */
    private void reject(Exception exception) {
        this.exception = exception;
        this.state = FAILED;
        synchronized (lock) {
            lock.notifyAll();
        }

        handleException(exception);
        postFulfillment();
    }

    private void handleException(Exception exception) {
        if (exceptionHandler != null) {
            exceptionHandler.accept(exception);
        }
    }

    private void postFulfillment() {
        if (fulfillmentAction != null) {
            fulfillmentAction.run();
        }
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return state > RUNNING;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (state == RUNNING) {
                lock.wait();
            }
        }
        if (state == COMPLETED) {
            return value;
        }
        throw new ExecutionException(exception);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws ExecutionException {
        synchronized (lock) {
            while (state == RUNNING) {
                try {
                    lock.wait(unit.toMillis(timeout));
                } catch (InterruptedException e) {
                    log.warn("Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (state == COMPLETED) {
            return value;
        }
        throw new ExecutionException(exception);
    }

    /**
     * Accesses the value from source promise and calls the consumer, then fulfills the
     * destination promise.
     */
    private class ConsumeAction implements Runnable {

        private final Promise<T> source;
        private final Promise<Void> target;
        private final Consumer<? super T> action;

        private ConsumeAction(Promise<T> source, Promise<Void> target, Consumer<? super T> action) {
            this.source = source;
            this.target = target;
            this.action = action;
        }

        @Override
        public void run() {
            try {
                action.accept(source.get());
                target.fulfill(null);
            } catch (Throwable throwable) {
                target.reject((Exception) throwable.getCause());
            }
        }
    }

    /**
     * Accesses the value from source promise, then fulfills the destination promise using the
     * transformed value. The source value is transformed using the transformation function.
     */
    private class TransformAction<V> implements Runnable {

        private final Promise<T> source;
        private final Promise<V> target;
        private final Function<? super T, V> func;

        private TransformAction(Promise<T> source, Promise<V> target, Function<? super T, V> func) {
            this.source = source;
            this.target = target;
            this.func = func;
        }

        @Override
        public void run() {
            try {
                target.fulfill(func.apply(source.get()));
            } catch (Throwable throwable) {
                target.reject((Exception) throwable.getCause());
            }
        }
    }
}