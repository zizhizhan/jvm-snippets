package com.zizhizhan.jni;

public class JniTests {

    public native void displayHelloWorld();

    static {
        System.loadLibrary("hello");
    }

    public static void main(String[] args) {
        new JniTests().displayHelloWorld();
    }
}
