package com.zizhizhan.legacies.pattern.proxy.dynamic.v0;

import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        Subject subject = createSubject();
        subject.handleRequest(new Request("james", "123456"));
        subject.handleRequest2(new Request("james", "123456"));
        subject.handleRequest3(new Request("james", "123456"));
        subject.handleRequest4(new Request("james", "123456"));
        subject.handleRequest5(new Request("james", "123456"));
    }

    public static Subject createSubject() {
        Subject real = new RealSubject();
        Subject logging = (Subject) Proxy.newProxyInstance(Client.class.getClassLoader(),
                new Class<?>[]{Subject.class}, new LoggingProxy(real));
        return (Subject) Proxy.newProxyInstance(Client.class.getClassLoader(),
                new Class<?>[]{Subject.class}, new AuthProxy(logging));
    }

}
