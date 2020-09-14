package com.zizhizhan.jvm.indy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/24/15
 *         Time: 6:27 PM
 */
public class MethodTypeSample2 {

    public static void main(String[] args) {
        MethodType mtToString = MethodType.methodType(String.class);
        MethodType mtSetter = MethodType.methodType(void.class, Object.class);
        MethodType mtStringComparator = MethodType.methodType(int.class, String.class, String.class);

        MethodHandles.Lookup caller = MethodHandles.lookup();
        try {
            MethodHandle methodHandle = caller.findVirtual(caller.lookupClass(), "toString", mtToString);
            Object target = methodHandle.invoke(new MethodTypeSample2());
            System.out.println(target);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
