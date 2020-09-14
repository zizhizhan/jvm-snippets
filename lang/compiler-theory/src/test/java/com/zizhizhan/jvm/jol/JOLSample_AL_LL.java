package com.zizhizhan.jvm.jol;

import com.google.common.collect.Lists;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * The example of traversing the reachability graphs.
 *
 * In addition to introspecting the object internals, we can also
 * see the object externals, that is, the objects referenced from the
 * object in question. There are multiple ways to illustrate this,
 * the summary table seems to work well.
 *
 * In this example, you can clearly see the difference between
 * the shadow heap (i.e. space taken by the reachable objects)
 * for ArrayList and LinkedList.
 *
 * When several roots are handed over to JOL, it tracks the objects reachable
 * from either root, and also avoids double-counting.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_AL_LL {

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        out.println(VM.current().details());

        List<Integer> al = Lists.newArrayList();
        List<Integer> ll = Lists.newArrayList();

        for (int i = 0; i < 1000; i++) {
            Integer io = i; // box once
            al.add(io);
            ll.add(io);
        }

        PrintWriter pw = new PrintWriter(out);
        pw.println(GraphLayout.parseInstance(al).toFootprint());
        pw.println(GraphLayout.parseInstance(ll).toFootprint());
        pw.println(GraphLayout.parseInstance(al, ll).toFootprint());
        pw.close();
    }
}
