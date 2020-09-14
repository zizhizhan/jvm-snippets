package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * This is another dive into the mark word.
 *
 * Among other things, mark words store locking information.
 * We can clearly see how the mark word contents change when
 * we acquire the lock, and release it subsequently.
 *
 * This one is the example of thin (displaced) lock. The data
 * in mark word when lock is acquired is the reference to the
 * displaced object header, allocated on stack. Once we leave
 * the lock, the displaced header is discarded, and mark word
 * is reverted to the default value.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_ThinLocking {

    public static void main(String[] args) throws Exception {
        System.out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        System.out.println("**** Fresh object");
        System.out.println(layout.toPrintable());

        synchronized (a) {
            System.out.println("**** With the lock");
            System.out.println(layout.toPrintable());
        }

        System.out.println("**** After the lock");
        System.out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}
