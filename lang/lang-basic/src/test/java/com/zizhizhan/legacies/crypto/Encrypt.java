package com.zizhizhan.legacies.crypto;

public class Encrypt {

    public static void main(String[] args) {
        System.out.println(encrypt("ABCD545BDaddfa"));
    }

    public static String encrypt(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + 'z' - c));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + 'Z' - c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
