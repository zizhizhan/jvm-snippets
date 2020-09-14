package com.zizhizhan.jvm.jol;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM2:43
 */
@Slf4j
public class JOLSamples {

    public static void main(String[] args) throws Exception {
        log.info(VM.current().details());
        log.info(ClassLayout.parseClass(Basic.class).toPrintable());
        log.info(ClassLayout.parseClass(Alignment.class).toPrintable());
        log.info(ClassLayout.parseClass(Packing.class).toPrintable());
        log.info(ClassLayout.parseClass(Inheritance.class).toPrintable());
        log.info(ClassLayout.parseClass(InheritanceBarrier.class).toPrintable());
        log.info(ClassLayout.parseClass(Gaps.class).toPrintable());
        log.info(ClassLayout.parseClass(Contended.class).toPrintable()); //-XX:-RestrictContended
        log.info(ClassLayout.parseClass(ClassWordA.class).toPrintable());
        log.info(ClassLayout.parseClass(ClassWordB.class).toPrintable());
    }

    public static class Basic {
        boolean f;
    }

    public static class Alignment {
        long f;
    }

    public static class Packing {
        boolean bo1, bo2;
        byte b1, b2;
        char c1, c2;
        double d1, d2;
        float f1, f2;
        int i1, i2;
        long l1, l2;
        short s1, s2;
    }


    public static class A {
        int a;
    }

    public static class B extends A {
        int b;
    }

    public static class Inheritance extends B {
        int c;
    }

    public static class A2 {
        long a;
    }

    public static class B2 extends A2 {
        long b;
    }

    public static class InheritanceBarrier extends B2 {
        long c;
        int d;
    }

    public static class A3 {
        boolean a;
    }

    public static class B3 extends A3 {
        boolean b;
    }

    public static class Gaps extends B3 {
        boolean c;
    }

    public static class A4 {
        int a;
        int b;
        @sun.misc.Contended  int c;
        int d;
    }

    public static class Contended extends A4 {
        int e;
    }

    public static class ClassWordA {
        // no fields
    }

    public static class ClassWordB {
        // no fields
    }
}
