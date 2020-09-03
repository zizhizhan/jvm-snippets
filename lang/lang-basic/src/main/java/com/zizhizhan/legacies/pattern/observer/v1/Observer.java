package com.zizhizhan.legacies.pattern.observer.v1;

@FunctionalInterface
public interface Observer<T, Args> {

    void update(T sender, Args args);

}
