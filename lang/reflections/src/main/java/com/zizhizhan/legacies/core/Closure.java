package com.zizhizhan.legacies.core;

public interface Closure<T> {

    void f(T obj) throws ClosureException;
}
