package com.zizhizhan.jvm.jol;

import com.google.common.collect.Lists;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM4:03
 */
public class JOLSample_Compaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOLSamples.class);

    public static volatile Object sink;

    /**
     * This example generates PNG images in tmp directory.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info(VM.current().details());


        // allocate some objects to beef up generations
        for (int c = 0; c < 1000000; c++) {
            sink = new Object();
        }
        System.gc();

        List<String> list = Lists.newArrayList();
        for (int c = 0; c < 1000; c++) {
            list.add("Key" + c);
        }

        for (int c = 1; c <= 10; c++) {
            GraphLayout.parseInstance(list).toImage("/tmp/list-" + c + ".png");
            System.gc();
        }
    }

}
