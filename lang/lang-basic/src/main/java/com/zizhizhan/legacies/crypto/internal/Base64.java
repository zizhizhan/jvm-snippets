package com.zizhizhan.legacies.crypto.internal;

public class Base64 {

    /**
     * The 64 valid Base64 characters
     */
    private final static byte[] BASE64_ALPHABET = {(byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
            (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
            (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
            (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
            (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
            (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
            (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
            (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
            (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
            (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
            (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
            (byte) '+', (byte) '/'};

    private static final byte SUFFIX_CODE = (byte) '=';

    public static String encode(byte[] a) {
        int len = a.length;
        int groups = len / 3;
        int remainingBytes = len - 3 * groups;
        int resultLength = 4 * ((len + 2) / 3);
        byte[] result = new byte[resultLength];

        int srcIndex = 0, dstIndex = 0;

        for (int i = 0; i < groups; i++) {
            int byte0 = a[srcIndex++] & 0xff;
            int byte1 = a[srcIndex++] & 0xff;
            int byte2 = a[srcIndex++] & 0xff;

            result[dstIndex++] = BASE64_ALPHABET[byte0 >> 2];
            result[dstIndex++] = BASE64_ALPHABET[(byte0 & 0x03) << 4 | (byte1 >> 4)];
            result[dstIndex++] = BASE64_ALPHABET[(byte1 << 2) & 0x3c | (byte2 >> 6)];
            result[dstIndex++] = BASE64_ALPHABET[byte2 & 0x3f];
        }

        if (remainingBytes > 0) {
            int byte0 = a[srcIndex++] & 0xff;
            result[dstIndex++] = BASE64_ALPHABET[byte0 >> 2];
            if (remainingBytes == 1) {
                result[dstIndex++] = BASE64_ALPHABET[(byte0 << 4) & 0x30];
                result[dstIndex++] = SUFFIX_CODE;
                result[dstIndex++] = SUFFIX_CODE;
            } else {
                // remainingBytes == 2;
                int byte1 = a[srcIndex++] & 0xff;
                result[dstIndex++] = BASE64_ALPHABET[(byte0 << 4) & 0x30 | (byte1 >> 4)];
                result[dstIndex++] = BASE64_ALPHABET[(byte1 << 2) & 0x3f];
                result[dstIndex++] = SUFFIX_CODE;
            }
        }

        return new String(result);
    }

    public static byte[] decode(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        int len = s.length();
        if (len % 4 != 0) {
            throw new IllegalArgumentException(
                    "String length must be a multiple of four.");
        }

        int groups = len / 4;
        int numOfFullGroups = groups;
        int numOfPaddings = 0;
        if (s.charAt(len - 1) == SUFFIX_CODE) {
            numOfPaddings++;
            numOfFullGroups--;
            if (s.charAt(len - 2) == SUFFIX_CODE) {
                numOfPaddings++;
            }
        }
        byte[] result = new byte[3 * groups - numOfPaddings];

        int srcIndex = 0, dstIndex = 0;

        for (int i = 0; i < numOfFullGroups; i++) {
            int ch0 = getCharIndex(s.charAt(srcIndex++));
            int ch1 = getCharIndex(s.charAt(srcIndex++));
            int ch2 = getCharIndex(s.charAt(srcIndex++));
            int ch3 = getCharIndex(s.charAt(srcIndex++));

            result[dstIndex++] = (byte) (ch0 << 2 | ch1 >> 4);
            result[dstIndex++] = (byte) (ch1 << 4 | ch2 >> 2);
            result[dstIndex++] = (byte) (ch2 << 6 | ch3);
        }

        if (numOfPaddings != 0) {
            int ch0 = getCharIndex(s.charAt(srcIndex++));
            int ch1 = getCharIndex(s.charAt(srcIndex++));
            result[dstIndex++] = (byte) ((ch0 << 2) | (ch1 >> 4));
            if (numOfPaddings == 1) {
                int ch2 = getCharIndex(s.charAt(srcIndex++));
                result[dstIndex++] = (byte) ((ch1 << 4) | (ch2 >> 2));
            }
        }


        return result;
    }

    private static int getCharIndex(char c) {
        if (c >= 'A' && c <= 'Z') // A-Z: 65-90
        {
            return (int) c - 65;
        } else if (c >= 'a' && c <= 'z') // a-z: 97-122
        {
            return (int) c - 71;
        } else if (c >= '0' && c <= '9') // 0-9: 48-57
        {
            return (int) c + 4;
        } else if (c == '+') {
            return 62;
        } else if (c == '/') {
            return 63;
        }
        throw new IllegalArgumentException("Character " + c
                + " is not a BASE64 char");
    }
}

