package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * This is the example of biased locking.
 *
 * In order to demonstrate this, we first need to sleep for >5 seconds
 * to pass the grace period of biased locking. Then, we do the same
 * trick as the example before. You may notice that the mark word
 * had not changed after the lock was released. That is because
 * the mark word now contains the reference to the thread this object
 * was biased to.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_BiasedLocking {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        out.println(VM.current().details());

        TimeUnit.SECONDS.sleep(6);

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        synchronized (a) {
            out.println("**** With the lock");
            out.println(layout.toPrintable());
        }

        out.println("**** After the lock");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}
