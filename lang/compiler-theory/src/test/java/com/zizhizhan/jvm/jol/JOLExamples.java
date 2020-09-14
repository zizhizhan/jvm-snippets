package com.zizhizhan.jvm.jol;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/5
 *         Time: PM2:59
 */
@Slf4j
public class JOLExamples {

    public static void main(String[] args) throws Exception {
        log.info(VM.current().details());
        log.info(ClassLayout.parseClass(Throwable.class).toPrintable());
        /**
         * @see http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/tip/src/share/vm/classfile/javaClasses.hpp
         * @see http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/tip/src/share/vm/classfile/javaClasses.cpp
         */
        log.info(ClassLayout.parseClass(Class.class).toPrintable());

        log.info(ClassLayout.parseInstance(new long[0]).toPrintable());
        for (int size = 0; size <= 8; size++) {
            log.info(ClassLayout.parseInstance(new byte[size]).toPrintable());
        }

    }

}
