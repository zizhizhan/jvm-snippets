package com.zizhizhan.util;

import java.util.Formatter;

public class HexUtils {

    public static String bytes2Hex(final byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

}
