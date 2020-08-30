package com.zizhizhan.text;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: James Zhan
 * Email: zhiqiangzhan@gmail.com
 * Date: 2019-01-04
 * Time: 00:05
 */
public class NumberFormat {

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("0.0");
        System.out.println(format.format(10));
        System.out.println(format.format(3.0 / 10));
        System.out.println(format.format(35 * 1.0 / 10));
    }

}
