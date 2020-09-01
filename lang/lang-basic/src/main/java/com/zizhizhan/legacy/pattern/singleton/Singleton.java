package com.zizhizhan.legacy.pattern.singleton;

public class Singleton {

    private static Singleton singleton;

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    protected Singleton() {
    }

    public static Singleton newInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

}
