package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class AsymmetricCiphers implements Algorithms {

    public static String sign(String algorithm, String privateK, String plainText) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateK.getBytes(StandardCharsets.UTF_8));
        KeyFactory keyFactory = KeyFactory.getInstance(findAlgorithmBy(algorithm));
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        byte[] signInfo = sign(algorithm, privateKey, plainText.getBytes(StandardCharsets.UTF_8));
        return HexUtils.encodeHexString(signInfo);
    }

    public static boolean verify(String algorithm, String publicK, String plainText, String signature) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicK.getBytes(StandardCharsets.UTF_8));
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        byte[] signBytes = HexUtils.decodeHex(signature);
        return verify(algorithm, publicKey, plainText.getBytes(StandardCharsets.UTF_8), signBytes);
    }

    public static byte[] sign(String algorithm, PrivateKey privateKey, byte[] plainBytes) {
        try {
            Signature sign = Signature.getInstance(algorithm);
            sign.initSign(privateKey);
            sign.update(plainBytes);
            return sign.sign();
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("[{}] sign error", algorithm, e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static boolean verify(String algorithm, PublicKey publicKey, byte[] plainBytes, byte[] signedBytes) {
        try {
            Signature sign = Signature.getInstance(algorithm);
            sign.initVerify(publicKey);
            sign.update(plainBytes);
            return sign.verify(signedBytes);
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("[{}] verify error.", algorithm, e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static KeyPair generateDHKeyPair() {
        try {
            return generateKeyPair(DH);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static KeyPair generateDSAeyPair() {
        try {
            return generateKeyPair(DSA);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static KeyPair generateRSAKeyPair() {
        try {
            return generateKeyPair(RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static KeyPair generateECKeyPair() {
        try {
            return generateKeyPair(EC);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static KeyPair generateKeyPair(String algorithm) throws NoSuchAlgorithmException {
        return generateKeyPair(algorithm, 1024);
    }

    public static KeyPair generateKeyPair(String algorithm, int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keysize);
        return generator.generateKeyPair();
    }

    private static String findAlgorithmBy(String signatureAlgorithm) {
        if (signatureAlgorithm.contains(EC)) {
            return EC;
        } else if (signatureAlgorithm.contains(DSA)) {
            return DSA;
        } else if (signatureAlgorithm.contains("DH")) {
            return DH;
        } else {
            return RSA;
        }
    }

}
