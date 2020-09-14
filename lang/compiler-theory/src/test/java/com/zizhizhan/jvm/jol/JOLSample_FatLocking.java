package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * This is the example of fat locking.
 *
 * If VM detects contention on thread, it needs to delegate the
 * access arbitrage to OS. That involves associating the object
 * with the native lock, i.e. "inflating" the lock.
 *
 * In this example, we need to simulate the contention, this is
 * why we have the additional thread. You can see the fresh object
 * has the default mark word, the object before the lock was already
 * acquired by the auxiliary thread, and when the lock was finally
 * acquired by main thread, it had been inflated. The inflation stays
 * there after the lock is released. You can also see the lock is
 * "deflated" after the GC (the lock cleanup proceeds in safepoints,
 * actually).
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_FatLocking {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });

        t.start();

        TimeUnit.SECONDS.sleep(1);

        out.println("**** Before the lock");
        out.println(layout.toPrintable());

        synchronized (a) {
            out.println("**** With the lock");
            out.println(layout.toPrintable());
        }

        out.println("**** After the lock");
        out.println(layout.toPrintable());

        System.gc();

        out.println("**** After System.gc()");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }
}
