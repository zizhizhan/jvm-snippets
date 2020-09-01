/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.zizhizhan.text;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 3/10/14
 *         Time: 12:55 AM
 */
public class EncodingTests {

    /**
     * (单字节编码)只需要确保每一个字节都有对应的字符定义时，这时，当我们拿到任意的字节序列时，我们都可以先把它映射成iso8859-1的string，
     * 然后根据内容推断出文本编码，或者做一些基本的过滤，或者筛选，当需要用原始字节数组时，可以毫无损失地还原回来。
     */
    @Test
    public void iso8859_1() {
        Charset iso8859_1 = Charset.forName("iso8859-1");  //Also can work for iso8859-2, iso8859-4, iso8859-5, iso8859-9
        byte[] bytes = new byte[256];
        for (int i = 0; i < 256; i++) {
            bytes[i] = (byte)i;
            System.out.println(bytes[i]);
        }
        String str = new String(bytes, iso8859_1);
        byte[] bytes2 = str.getBytes(iso8859_1);
        Assert.assertArrayEquals(bytes, bytes2);
    }

}
