package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.*;
import java.util.Base64;

//import sun.security.rsa.RSAPrivateCrtKeyImpl;
//import sun.security.rsa.RSAPublicKeyImpl;

@Slf4j
public class RsaTests {

    @Test
    public void loadRSAKeyPair() throws InvalidKeyException {
        KeyPair rsaKeyPair = RsaUtils.generateRsaKeyPair();
        log.info("RSA:(\n\tpublic: {}, \n\tprivate: {}\n)",
                Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded()),
                Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded()));

//        PublicKey publicKey = new RSAPublicKeyImpl(rsaKeyPair.getPublic().getEncoded());
//        PrivateKey privateKey = RSAPrivateCrtKeyImpl.newKey(rsaKeyPair.getPublic().getEncoded());
//        log.info("RSA:(\n\tpublic: {}, \n\tprivate: {}\n)",
//                Base64.getEncoder().encodeToString(publicKey.getEncoded()),
//                Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

    @Test
    public void generateKeyPair() throws NoSuchAlgorithmException {
        generateKeyPair("DiffieHellman", 1024);
        generateKeyPair("DSA", 1024);
        generateKeyPair("RSA", 1024);
        generateKeyPair("EC", 256);
    }

    private void generateKeyPair(String algorithm, int keysize) throws NoSuchAlgorithmException {
        KeyPair rsaKeyPair = RsaUtils.generateKeyPair(algorithm, keysize);
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        log.info("{}:(\n\tpublic: {}, \n\tprivate: {}\n)", algorithm, publicKey, privateKey);
        log.info("PublicAlgorithm: {}/{}\n{}", publicKey.getAlgorithm(), publicKey.getFormat(),
                Base64.getEncoder().encodeToString(publicKey.getEncoded()));

        log.info("PrivateAlgorithm: {}/{}\n{}\n", privateKey.getAlgorithm(), privateKey.getFormat(),
                Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

}
