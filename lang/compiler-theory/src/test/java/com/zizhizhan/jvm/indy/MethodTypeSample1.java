package com.zizhizhan.jvm.indy;

import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/23/15
 *         Time: 11:31 PM
 */
@Slf4j
public class MethodTypeSample1 {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();

        MethodHandle method = caller.findStatic(MethodTypeSample1.class, "hello", MethodType.methodType(String.class, String.class));
        Object obj = method.invoke("world");
        System.out.println(obj);
    }

    public static String hello(String name) {
        log.debug("Calling hello {}.", name);
        return "Hello " + name + "!";
    }

}
