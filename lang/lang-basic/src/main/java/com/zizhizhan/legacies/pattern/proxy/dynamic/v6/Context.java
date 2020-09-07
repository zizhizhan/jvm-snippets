package com.zizhizhan.legacies.pattern.proxy.dynamic.v6;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class Context {

    public static void main(String[] args) {
        Subject realSubject = new RealSubject();
        Subject owner = getOwnerProxy(realSubject);
        Subject nonOwner = getNonOwnerProxy(realSubject);

        owner.execute();
        nonOwner.execute();
    }

    public static Subject getOwnerProxy(Subject subject) {
        return (Subject) Proxy.newProxyInstance(getClassLoader(), subject.getClass().getInterfaces(),
                new OwnerInvocationHandler(subject));
    }

    public static Subject getNonOwnerProxy(Subject subject) {
        return (Subject) Proxy.newProxyInstance(getClassLoader(), subject.getClass().getInterfaces(),
                new NonOwnerInvocationHandler());
    }


    private static ClassLoader getClassLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader());
    }

}
