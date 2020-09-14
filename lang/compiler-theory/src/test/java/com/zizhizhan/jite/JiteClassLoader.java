package com.zizhizhan.jite;

import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/6
 *         Time: PM12:32
 */
public class JiteClassLoader extends ClassLoader {

    public Class<?> define(JiteClass jiteClass) {
        return define(jiteClass, JDKVersion.V1_7);
    }

    public Class<?> define(JiteClass jiteClass, JDKVersion jdkVersion) {
        byte[] classBytes = jiteClass.toBytes(jdkVersion);
        return super.defineClass(jiteClass.getClassName(), classBytes, 0, classBytes.length);
    }

}
