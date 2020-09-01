package com.zizhizhan.legacies.pattern.proxy.dynamic;

import java.lang.reflect.Method;

public class HelloHandler implements IHandler {

    public void start(Method method) {
        System.out.println(method.getName() + " start !");
    }

    public void end(Method method) {
        System.out.println(method.getName() + " end !");
    }

}
