package com.zizhizhan.lang;

import java.io.UnsupportedEncodingException;

public class EncodingMain {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "张三";
        //使用双字节编码对它进行解码
        byte[] b = a.getBytes("gb2312");
        //使用单字节编码对它进行编码
        String s = new String(b, "iso8859-1");
        //输出 4
        System.out.println(s.length());

        //使用iso8859-1的好处是它不会丢失信息，还可以将它还原

        System.out.println(new String(s.getBytes("iso8859-1"), "gb2312"));
        //输出 张三
    }
}


