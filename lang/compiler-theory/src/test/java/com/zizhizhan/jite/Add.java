package com.zizhizhan.jite;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import static me.qmx.jitescript.util.CodegenUtils.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/3/15
 *         Time: 11:50 PM
 */
public class Add {

    public interface Addable {
        int add(int a, int b);
    }

    public static void main(String[] args) throws Exception {
        JiteClass jiteClass = new JiteClass("Adder", new String[]{p(Addable.class)}) {
            {
                defineDefaultConstructor();
                defineMethod("add", ACC_PUBLIC, sig(int.class, int.class, int.class), new CodeBlock(){
                    {
                        iload(1);
                        iload(2);
                        iadd();
                        ireturn();
                    }
                });
            }
        };

        JiteClassLoader classLoader = new JiteClassLoader();
        Class<?> addClass = classLoader.define(jiteClass, JDKVersion.V1_7);

        Addable adder = (Addable)addClass.newInstance();
        System.out.println(adder.add(1, 2));
        System.out.println(adder.add(5, 7));
        System.out.println(adder.add(3, 4));
    }
}
