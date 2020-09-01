package com.zizhizhan.legacies.pattern.observer;

@FunctionalInterface
public interface Observer<T, Args> {

    void update(T sender, Args args);

}
