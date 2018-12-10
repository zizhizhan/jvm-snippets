package me.jameszhan.pattern.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Unsafes {

    public static Unsafe getUnsafe() {
        Unsafe unsafe =null;
        try{
            Class<?> clazz = Unsafe.class;
            Field f = clazz.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return  (Unsafe) f.get(clazz);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
