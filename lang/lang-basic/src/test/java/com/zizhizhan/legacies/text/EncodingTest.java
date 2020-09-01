package com.zizhizhan.legacies.text;

import java.io.IOException;
import java.nio.charset.Charset;

public class EncodingTest {

    public static void main(String[] args) throws IOException {
        System.out.println(Charset.defaultCharset());
		
	/*	InputStream in = EncodingTest.class.getResourceAsStream("1.html");
		byte[] buf = new byte[80000];
		int len = in.read(buf);

		String content = new String(buf, 0, len, "iso8859-1");
		String con = new String(content.getBytes("iso8859-1"), "utf-8");
		System.out.println(con);*/

        String hello = "因此不存在字符串的内码的问题";
        byte[] x = hello.getBytes("utf-8");
        String cache = new String(x, "iso8859-1");

        byte[] y = cache.getBytes("iso8859-1");

        System.out.println(new String(y, "utf-8"));
    }
}
