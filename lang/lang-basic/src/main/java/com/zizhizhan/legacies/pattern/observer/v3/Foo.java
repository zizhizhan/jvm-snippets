package com.zizhizhan.legacies.pattern.observer.v3;

public class Foo {

    public static void main(String[] args) {

        FooListener foo = EventMulticaster.add(new SomeListener("X"), null);
        foo = EventMulticaster.add(foo, new SomeListener("Y"));
        foo = EventMulticaster.add(foo, new SomeListener("Z"));
        FooListener fl = new SomeListener("Hello World!");
        foo = EventMulticaster.add(foo, fl);
        foo.foo();

        foo = EventMulticaster.remove(foo, fl);
        foo.foo();
    }
}

