package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class MessageDigests implements Algorithms {

    public static byte[] hash192(byte[] bytes) {
        return Base64.getEncoder().encode(md5sum(bytes));
    }

    public static String md2sum(String text) {
        return digest(MD2, text);
    }

    public static byte[] md2sum(byte[] bytes) {
        return digest(MD2, bytes);
    }

    public static String md4sum(String text) {
        byte[] bytes = md4sum(text.getBytes(StandardCharsets.UTF_8));
        return HexUtils.encodeHexString(bytes);
    }

    public static byte[] md4sum(byte[] bytes) {
        return digest(sun.security.provider.MD4.getInstance(), bytes);
    }

    public static String md5sum(String text) {
        return digest(MD5, text);
    }

    public static byte[] md5sum(byte[] bytes) {
        return digest(MD5, bytes);
    }

    public static String shasum(String text) {
        return digest(SHA, text);
    }

    public static byte[] shasum(byte[] bytes) {
        return digest(SHA, bytes);
    }

    public static String sha224sum(String text) {
        return digest(SHA224, text);
    }

    public static byte[] sha224sum(byte[] bytes) {
        return digest(SHA224, bytes);
    }

    public static String sha256sum(String text) {
        return digest(SHA256, text);
    }

    public static byte[] sha256sum(byte[] bytes) {
        return digest(SHA256, bytes);
    }

    public static String sha384sum(String text) {
        return digest(SHA384, text);
    }

    public static byte[] sha384sum(byte[] bytes) {
        return digest(SHA384, bytes);
    }

    public static String sha512sum(String text) {
        return digest(SHA512, text);
    }

    public static byte[] sha512sum(byte[] bytes) {
        return digest(SHA512, bytes);
    }

    public static String digest(String algorithm, String text) {
        byte[] bytes = digest(algorithm, text.getBytes(StandardCharsets.UTF_8));
        return HexUtils.encodeHexString(bytes);
    }

    public static byte[] digest(String algorithm, byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static byte[] digest(MessageDigest messageDigest, byte[] bytes) {
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    public static void main(String[] args) {
        System.out.println(shasum("Helloworld="));
    }

}
