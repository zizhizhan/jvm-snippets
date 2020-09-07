package com.zizhizhan.legacies.pattern.proxy.dynamic.v6;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NonOwnerInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("You are not the owner!");
        System.out.println("Reject...");
        return null;
    }

}
