package com.zizhizhan.jvm.jol;

import org.openjdk.jol.datamodel.X86_32_DataModel;
import org.openjdk.jol.datamodel.X86_64_COOPS_DataModel;
import org.openjdk.jol.datamodel.X86_64_DataModel;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.layouters.CurrentLayouter;
import org.openjdk.jol.layouters.HotSpotLayouter;
import org.openjdk.jol.layouters.Layouter;

/**
 * This example shows the differences between the data models.
 *
 * First layout is the actual VM layout, the remaining three
 * are simulations. You can see the reference sizes are different,
 * depending on VM bitness or mode. The header sizes are also
 * a bit different, see subsequent examples to understand why.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:06
 */
public class JOLSample_DataModels {


    public static void main(String[] args) throws Exception {
        Layouter l;

        l = new CurrentLayouter();
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_32_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_64_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_64_COOPS_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
    }

    public static class A {
        Object a;
        Object b;
    }
}
