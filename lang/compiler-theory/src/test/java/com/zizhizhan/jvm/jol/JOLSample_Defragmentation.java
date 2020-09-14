package com.zizhizhan.jvm.jol;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:00
 */
public class JOLSample_Defragmentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOLSamples.class);

    public static volatile Object sink;

    public static void main(String[] args) throws Exception {
        LOGGER.info(VM.current().details());

        // allocate some objects to beef up generations
        for (int c = 0; c < 1000000; c++) {
            sink = new Object();
        }
        System.gc();

        final int COUNT = 10000;

        Object[] array = new Object[COUNT];
        for (int c = 0; c < COUNT; c++) {
            array[c] = new Object();
        }

        Object obj = array;

        GraphLayout.parseInstance(obj).toImage("/tmp/array-1-new.png");

        for (int c = 2; c <= 5; c++) {
            System.gc();
            GraphLayout.parseInstance(obj).toImage("/tmp/array-" + c + "-before.png");
        }

        for (int c = 0; c < COUNT; c++) {
            if (Math.random() < 0.5) {
                array[c] = null;
            }
        }

        GraphLayout.parseInstance(obj).toImage("/tmp/array-6-after.png");

        for (int c = 7; c <= 10; c++) {
            System.gc();
            GraphLayout.parseInstance(obj).toImage("/tmp/array-" + c + "-after-gc.png");
        }
    }
}
