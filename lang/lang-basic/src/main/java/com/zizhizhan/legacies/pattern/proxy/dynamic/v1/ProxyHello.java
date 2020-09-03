package com.zizhizhan.legacies.pattern.proxy.dynamic.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHello implements InvocationHandler {

    private Object hello;

    private Object handler;

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Object bind(Object hello, Object handler) {
        setHello(hello);
        setHandler(handler);
        Class<?> cls = hello.getClass();
        return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("dddd");
        //((IHandler) handler).start(method);
        //Class<?> cls = handler.getClass();
        //Method func = cls.getDeclaredMethod("start", new Class<?>[]{Method.class});
        //func.invoke(handler, new Object[]{method});
        Object ret = method.invoke(hello, args);
        //((IHandler) handler).end(method);
        System.out.println("eddd");
        return ret;
    }

    public void setHello(Object hello) {
        this.hello = hello;
    }

    public static void main(String[] args) {
        IHello hello = (IHello) new ProxyHello().bind(new Hello(),
                new HelloHandler());

        hello.sayHello("Double J");

        hello.sayGoodbye("Thirteen");

    }

}
