package com.zizhizhan.jite;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import org.objectweb.asm.Opcodes;

import static me.qmx.jitescript.util.CodegenUtils.sig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/6
 *         Time: PM8:02
 */
public class Fibonacci {

    public static void main(String[] args) {
        JiteClass jiteClass = new JiteClass("Fibonacci") {
            {
                defineDefaultConstructor();
                defineMethod("add", ACC_PUBLIC | Opcodes.ACC_STATIC, sig(long.class, long.class), new CodeBlock() {
                    {
                        iload(0);
                        iconst_0();
                        lcmp();

                        iadd();
                        ireturn();
                    }
                });
            }
        };

        JiteClassLoader classLoader = new JiteClassLoader();
        Class<?> addClass = classLoader.define(jiteClass, JDKVersion.V1_7);
        // todo
    }

}
