package com.zizhizhan.legacies.pattern.proxy.dynamic;

public class Hello implements IHello {

    public void sayHello(String name) {
        System.out.println("hello " + name);
    }

    public void sayGoodbye(String name) {
        System.out.println("goodbye, " + name);
    }

}
