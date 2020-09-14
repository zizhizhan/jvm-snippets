package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * This example shows the array layout quirks.
 *
 * If you run it with parallel GC, you might notice that
 * fresh object elements are laid out after the array in
 * the forward order, but after GC then can be rearranged
 * in the reverse order. This is because GC records the
 * to-be-promoted objects on the stack.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_Arrays {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        Integer[] arr = new Integer[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Integer(i);
        }

        String last = null;
        for (int c = 0; c < 100; c++) {
            String current = GraphLayout.parseInstance(arr).toPrintable();

            if (last == null || !last.equalsIgnoreCase(current)) {
                pw.println(current);
                last = current;
            }

            System.gc();
        }

        pw.close();
    }
}
