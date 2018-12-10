package me.jameszhan.pattern.promise;

import java.util.function.Consumer;

public interface PromiseExecutor<T> {

    void execute(Consumer<T> resolve, Consumer<Throwable> reject);

}
