package com.zizhizhan.legacies.pattern.proxy.dynamic.v0;

import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    public static void main(String[] args) {
        CharSequence cs = (CharSequence) Proxy.newProxyInstance(Client.class.getClassLoader(),
                new Class<?>[]{CharSequence.class, Comparable.class}, new LoggingProxy("Hello World."));
        System.out.println(cs.toString());
        System.out.println(cs.length());

        System.out.println(((Comparable<String>)cs).compareTo("Hello World."));
    }
}
