package com.zizhizhan.legacies.core.reflect;

import java.lang.reflect.Method;

public class InvokeTester {

    private int initValue = 100;

    public int add(int param1, int param2) {
        return param1 + param2;
    }

    public String echo(String msg) {
        return "echo: " + msg;
    }

    public InvokeTester() {

    }

    public int getInitValue() {
        return initValue;
    }

    public void setInitValue(int initValue) {
        this.initValue = initValue;
    }

    public InvokeTester(int initValue) {
        super();
        this.initValue = initValue;
    }

    public int selfAdd() {
        initValue++;
        return initValue;
    }

    public static void main(String[] args) throws Exception {
        Class<?> classType = InvokeTester.class;
        Object invokeTester = classType.newInstance();

        //Method addMethod = classType.getMethod("add", new Class[] { int.class,
        //int.class });
        Method addMethod = classType.getMethod("add", int.class, int.class);
        Object result = addMethod.invoke(invokeTester, new Integer(100), new Integer(200));

        System.out.println((Integer) result);

        Method echoMethod = classType.getMethod("echo",
				String.class);
        result = echoMethod.invoke(invokeTester, "Hello");
        System.out.println((String) result);

        InvokeTester test = new InvokeTester();
        test.setInitValue(105);

        Method selfAddMethod = classType.getMethod("selfAdd");

        System.out.println(selfAddMethod.invoke(test));
        System.out.println(selfAddMethod.invoke(test));
        System.out.println(selfAddMethod.invoke(invokeTester));
    }

}
