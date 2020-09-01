package com.zizhizhan.legacy.pattern.adapter;

public class Adapter {
    void GenericClientCode(FrameworkXTarget x) {
        x.SomeRequest(4);
    }

    public static void main(String[] args) {
        Adapter c = new Adapter();
        FrameworkXTarget x = new OurAdapter();
        c.GenericClientCode(x);
    }
}
