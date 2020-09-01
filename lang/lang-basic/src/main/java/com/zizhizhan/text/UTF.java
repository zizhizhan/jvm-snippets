package com.zizhizhan.text;

/**
 * UCS (Universal Character Set)
 * UTF (UCS Transformation Format)
 * <p>
 * |--------------------------------|-------------------------------|
 * |	UCS-2字符编码（十六进制数）		|	UTF-8字节流（二进制数）			|
 * |--------------------------------|-------------------------------|
 * |	0000-007F					|	0xxxxxxx					|
 * |	0080-07FF					|	110xxxxx 10xxxxxx			|
 * |	0800-FFFF					|	1110xxxx 10xxxxxx 10xxxxxx	|
 * |--------------------------------|-------------------------------|
 */

public class UTF {

    private static final int SINGLE = 0x0080;
    private static final int DOUBLE = 0x0800;

    public static String unicode2utf(char c) {
        if (c < SINGLE) {
            return Integer.toHexString(c);
        } else if (c < DOUBLE) {
            int a = c & 0x3F; // 111111
            a |= 0x80;
            int b = c & 0x7CF; // 11111 000000
            b <<= 2;
            b |= 0xC0;
            return Integer.toHexString(b | a);
        } else {
            int a = c & 0x3F;
            a |= 0x80;
            int b = c & 0xFC0;
            b <<= 2;
            b |= 0x8000;
            int x = c & 0xF000;
            x <<= 4;
            x |= 0xE00000;
            return Integer.toHexString(a | b | x);
        }
    }

    public static String utf2unicode(int u) {
        int len = Integer.toBinaryString(u).length();
        if (len < 8) {
            return Integer.toHexString(u);
        } else if (len == 16) {
            int a = u & 0x3F;
            int b = u & 0x1F00;
            b >>= 2;
            return Integer.toHexString(a | b);
        } else if (len == 24) {
            int a = u & 0x3F;
            int b = u & 0x3F00;
            int c = u & 0x0F0000;
            b >>= 2;
            c >>= 4;
            return Integer.toHexString(a | b | c);
        }
        return "";

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 19900; i < 21000; i++) {
            System.out.println(i + ":" + (char) i);
        }
        System.out.println(unicode2utf('＃'));
        System.out.println(utf2unicode(Integer.parseInt(unicode2utf('＃'), 16)));

        System.out.println(Integer.toBinaryString(6).length());

        String a = "dasafasfsa";
        String b = new String("dasafasfsa");
        System.out.println(a.equals(b));
    }
}
