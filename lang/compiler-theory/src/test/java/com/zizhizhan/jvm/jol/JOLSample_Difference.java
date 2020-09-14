package com.zizhizhan.jvm.jol;

import com.google.common.collect.Maps;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM3:24
 */
public class JOLSample_Difference {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOLSamples.class);

    public static void main(String[] args) {
        LOGGER.info(VM.current().details());

        Map<String, String> chm = Maps.newConcurrentMap();

        GraphLayout gl1 = GraphLayout.parseInstance(chm);

        chm.put("Foo", "Bar");
        GraphLayout gl2 = GraphLayout.parseInstance(chm);

        chm.put("Foo2", "Bar2");
        GraphLayout gl3 = GraphLayout.parseInstance(chm);

        System.out.println(gl2.subtract(gl1).toPrintable());
        System.out.println(gl3.subtract(gl2).toPrintable());
        System.out.println(gl3.subtract(gl3).toPrintable());
    }
}
