package com.zizhizhan.legacies.util;

import java.lang.reflect.Array;


public class ArrayTest {

    /**
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {

        System.out.println("Hello world");

        Class<?> stringType = Class.forName("java.lang.String");

        Object array = Array.newInstance(stringType, 10);

        // Object array = Array.newInstance(String.class, 10);

        Array.set(array, 5, "hello");

        String s = (String) Array.get(array, 5);
        System.out.println(s);

        int[] dims = new int[]{5, 10, 15};

        Object myarray = Array.newInstance(Integer.TYPE, dims);

        Object obj = Array.get(myarray, 3);

        Class<?> cls = obj.getClass().getComponentType();
        System.out.println(cls.toString());

        obj = Array.get(obj, 5);

        Array.setInt(obj, 10, 40);

        int[][][] castArray = (int[][][]) myarray;

        System.out.println(castArray[3][5][10]);

    }


}
