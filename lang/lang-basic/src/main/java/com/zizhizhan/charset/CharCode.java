package com.zizhizhan.charset;

public class CharCode {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String strUnicode = "AAAAA";
        println(strUnicode);
        try {
            byte[] buf = strUnicode.getBytes("GB2312");
            println(buf);
            System.out.println("----------------------------");
            byte[] buf1 = strUnicode.getBytes("UTF8");
            println(buf1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void println(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(Integer.toHexString(str.charAt(i)));
        }
    }

    public static void println(byte[] str) {
		for (byte b : str) {
			System.out.println(Integer.toHexString(b));
		}
    }

}
