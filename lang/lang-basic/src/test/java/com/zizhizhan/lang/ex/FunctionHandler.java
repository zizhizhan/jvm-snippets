package com.zizhizhan.lang.ex;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class FunctionHandler implements InvocationHandler {

    private final IFunction fun;

    public FunctionHandler(IFunction function) {
        this.fun = function;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(fun, args);
        } catch (Throwable e) {
            //throw e;
            throw e.getCause();
        }
    }
}
