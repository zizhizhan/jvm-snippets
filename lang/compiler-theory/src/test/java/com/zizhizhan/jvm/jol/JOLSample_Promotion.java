package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;

/**
 * The example of object promotion.
 *
 * Once the object survives the garbage collections, it is getting
 * promoted to another generation. In this example, we can track
 * the addresses of the objects, as it changes over time.
 *
 * VM also needs to record the "age" (that is, the number of GC
 * cycles the object had survived) of the object somewhere, and
 * it is stored in mark word as well. See how particular mark word
 * bits change with each promotion.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_Promotion {

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        System.out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        Object o = new Object();

        ClassLayout layout = ClassLayout.parseInstance(o);

        long lastAddr = VM.current().addressOf(o);
        pw.printf("Fresh object is at %x%n", lastAddr);

        int moves = 0;
        for (int i = 0; i < 100000; i++) {
            long cur = VM.current().addressOf(o);
            if (cur != lastAddr) {
                moves++;
                pw.printf("*** Move %2d, object is at %x%n", moves, cur);
                System.out.println(layout.toPrintable());
                lastAddr = cur;
            }

            // make garbage
            for (int c = 0; c < 10000; c++) {
                sink = new Object();
            }
        }

        pw.close();
    }
}
