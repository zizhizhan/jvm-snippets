package me.jameszhan.pattern.promise.release;

import java.util.function.Consumer;

public interface PromiseExecutor<T> {

    void execute(Promise<T> promise, Consumer<T> resolve, Consumer<Throwable> reject);

}
