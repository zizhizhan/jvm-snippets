package com.zizhizhan.legacies.pattern.proxy.hybrid;

public class RealSubject implements Subject {

    public void request() {
        System.out.println("Request is running in RealSubject: func request");
    }


}
