package com.zizhizhan.reflect;

import java.util.ArrayList;
import java.util.List;

class Foo {

    private final String s = "cctv";
    private final List<String> list = new ArrayList<>();
    private final static String test = "test";

    public Foo() {
        list.add("beijing");
        list.add("shanghai");
        list.add("shenzhen");
        list.add("guangzhou");
    }

    public List<String> getList() {
        return list;
    }

    public String getS() {
        return s;
    }

    public String getTest() {
        return test;
    }
}
