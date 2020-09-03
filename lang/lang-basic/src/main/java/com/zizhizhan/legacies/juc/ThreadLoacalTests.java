package com.zizhizhan.legacies.juc;

import java.util.HashMap;
import java.util.Map;

public class ThreadLoacalTests {

    private final static ThreadLocal<Map<String, String>> MAP = new ThreadLocal<Map<String, String>>();

    static {
        MAP.set(new HashMap<String, String>());
        MAP.get().put("abc", "123");
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            public void run() {
                System.out.println(MAP.get());
                MAP.set(new HashMap<String, String>());
                MAP.get().put("abc", "124");
                System.out.println(MAP.get().get("abc"));
            }
        }.start();

        Thread.sleep(100);

        System.out.println(MAP.get().get("abc"));
    }

}
