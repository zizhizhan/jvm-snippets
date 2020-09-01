package com.zizhizhan.legacy.scanner;

public interface Closure<T> {

    void f(T obj) throws ClosureException;
}
