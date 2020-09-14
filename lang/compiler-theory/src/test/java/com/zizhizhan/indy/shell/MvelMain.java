package com.zizhizhan.indy.shell;

import org.mvel2.MVEL;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 10/16/15
 *         Time: 11:20 PM
 */
public class MvelMain {

    public static void main(String[] args) {
        Object obj = MVEL.eval("1 + 2;");
        System.out.println(obj);
    }

}
