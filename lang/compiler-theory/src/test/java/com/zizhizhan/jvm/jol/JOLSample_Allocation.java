package com.zizhizhan.jvm.jol;

import org.openjdk.jol.vm.VM;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * The example of allocation addresses.
 *
 * This example shows the addresses of newly allocated objects
 * grow linearly in HotSpot. This is because the allocation in
 * parallel collectors is linear. We can also see it rewinds back
 * to the same offsets -- that's the start of some GC generation.
 * The address of the generation is changing, while GC adjusts
 * for the allocation rate.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_Allocation {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;

        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(out, true);

        long last = VM.current().addressOf(new Object());
        for (int l = 0; l < 1000 * 1000 * 1000; l++) {
            long current = VM.current().addressOf(new Object());

            long distance = Math.abs(current - last);
            if (distance > 16 * 1024) {
                pw.printf("Jumping from %x to %x (distance = %d bytes, %dK, %dM)%n",
                        last,
                        current,
                        distance,
                        distance / 1024,
                        distance / 1024 / 1024);
            }

            last = current;
        }

        pw.close();

    }
}
