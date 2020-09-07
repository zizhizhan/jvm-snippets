package com.zizhizhan.legacies.pattern.proxy.dynamic.v7;

import com.zizhizhan.legacies.util.ReflectionHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Context {

    private Request req;

    Request getRequest() {
        return (Request) Proxy.newProxyInstance(ReflectionHelper.getContextClassLoader(),
                new Class<?>[]{Request.class}, new DefaultInvocationHandler());
    }

    public void setRequest(Request request) {
        req = request;
    }

    class DefaultInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(req, args);
        }

    }

}

