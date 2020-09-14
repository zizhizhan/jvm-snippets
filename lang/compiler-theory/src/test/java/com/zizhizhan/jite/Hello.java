package com.zizhizhan.jite;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import java.io.PrintStream;
import java.lang.reflect.Method;

import static me.qmx.jitescript.util.CodegenUtils.*;
import static me.qmx.jitescript.CodeBlock.newCodeBlock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/6
 *         Time: PM12:29
 */
public class Hello {

    public static void main(String[] args) throws Exception {
        JiteClass jiteClass = new JiteClass("Hello") {
            {
                // you can use the pre-constructor style
                defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(void.class, String[].class), new CodeBlock() {
                    {
                        ldc("Hello World!");
                        getstatic(p(System.class), "out", ci(PrintStream.class));
                        swap();
                        invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
                        voidreturn();
                    }
                });
                // or use chained api
                defineMethod("hello", ACC_PUBLIC | ACC_STATIC, sig(String.class), newCodeBlock().ldc("Hello World!").areturn());
            }
        };

        JiteClassLoader classLoader = new JiteClassLoader();
        Class<?> helloClass = classLoader.define(jiteClass, JDKVersion.V1_7);

        Method hello = helloClass.getMethod("hello");
        System.out.println(hello.invoke(null));

        Method main = helloClass.getMethod("main", String[].class);
        main.invoke(null, (Object)args);
    }

}
