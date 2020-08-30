package com.zizhizhan.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public final class RsaUtils {

    public static final String RSA_ALGORITHM = "RSA";

    public static KeyPair generateRsaKeyPair() {
        try {
            return generateKeyPair(RSA_ALGORITHM);
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

//    public static KeyPair generateKeyPair(String algorithm, int keysize) throws NoSuchAlgorithmException {
//        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
//        generator.initialize(keysize);
//        return generator.generateKeyPair();
//    }

}
