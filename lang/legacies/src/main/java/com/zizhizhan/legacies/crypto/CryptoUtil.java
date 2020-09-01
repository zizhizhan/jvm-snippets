package com.zizhizhan.legacies.crypto;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class CryptoUtil implements Crypto {

    //private static Logger LOGGER = Logger.getLogger(CryptoUtil.class);
    private Encryption crypto;

    public String encrypt(String input) {
        return crypto.encrypt(stringToByteList(input));
    }

    public String decrypt(String input) {
        return byteListToString(crypto.decrypt(input));
    }

    public void setCrypto(Encryption crypto) {
        this.crypto = crypto;
    }

    public static List<Byte> stringToByteList(String input) {
        List<Byte> output = new ArrayList<Byte>();
        byte[] buf = input.getBytes();
        for (byte b : buf) {
            output.add(b);
        }
        return output;
    }

    public static String byteListToString(List<Byte> input) {
        return byteListToString(input, Charset.defaultCharset());
    }

    public static String byteListToString(List<Byte> input, Charset charset) {
        byte[] buf = new byte[input.size()];
        int i = 0;
        for (byte b : input) {
            buf[i++] = b;
        }
        return new String(buf, charset);
    }

}
