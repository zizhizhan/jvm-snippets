package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintStream;

/**
 * The example for identity hash code.
 *
 * The identity hash code, once computed, should stay the same.
 * HotSpot opts to store the hash code in the mark word as well.
 * You can clearly see the hash code bytes in the header once
 * it was computed.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_IdentityHashCode {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        out.println("hashCode: " + Integer.toHexString(a.hashCode()));
        out.println();

        out.println("**** After identityHashCode()");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}
