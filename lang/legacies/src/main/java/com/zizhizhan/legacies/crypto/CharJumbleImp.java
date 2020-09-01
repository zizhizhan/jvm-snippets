package com.zizhizhan.legacies.crypto;

public class CharJumbleImp implements CharJumble {

    private static final char[] UC_JUMBLE_CHARS = {'M', 'D', 'X', 'U', 'P',
            'I', 'B', 'E', 'J', 'C', 'T', 'N', 'K', 'O', 'G', 'W', 'R', 'S',
            'F', 'Y', 'V', 'L', 'Z', 'Q', 'A', 'H'};

    private static final char[] LC_JUMBLE_CHARS = {'m', 'd', 'x', 'u', 'p',
            'i', 'b', 'e', 'j', 'c', 't', 'n', 'k', 'o', 'g', 'w', 'r', 's',
            'f', 'y', 'v', 'l', 'z', 'q', 'a', 'h'};

    private static char[] UC_UNJUMBLE_CHARS = new char[26];

    private static char[] LC_UNJUMBLE_CHARS = new char[26];

    static {
        for (int i = 0; i < 26; i++) {
            char b = UC_JUMBLE_CHARS[i];
            UC_UNJUMBLE_CHARS[b - 'A'] = (char) ('A' + i);

            b = LC_JUMBLE_CHARS[i];
            LC_UNJUMBLE_CHARS[b - 'a'] = (char) ('a' + i);
        }
    }

    public char jumble(char b) {
        if (b >= 'A' && b <= 'Z') {
            return UC_JUMBLE_CHARS[b - 'A'];
        } else if (b >= 'a' && b <= 'z') {
            return LC_JUMBLE_CHARS[b - 'a'];
        } else {
            return b;
        }
    }

    public char unJumble(char b) {
        if (b >= 'A' && b <= 'Z') {
            return UC_UNJUMBLE_CHARS[b - 'A'];
        } else if (b >= 'a' && b <= 'z') {
            return LC_UNJUMBLE_CHARS[b - 'a'];
        } else {
            return b;
        }
    }

}
