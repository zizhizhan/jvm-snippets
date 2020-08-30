package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
public class SymmetricCiphersTests implements Algorithms {

    @Test
    public void generateSecretKey() {
        generateSecretKey(DES, 56);
        generateSecretKey(Triple_DES, 112);
        generateSecretKey(Triple_DES, 168);
        generateSecretKey(AES, 128);
        generateSecretKey(AES, 192);
        generateSecretKey(AES, 256);
    }

    @Test
    public void allEncryptModes() {
        String[] algorithms = { DES, Triple_DES, AES };
        String[] modes = { "ECB", "CBC", "CTS", "CTR", "CFB", "OFB", "PCBC" };
        String password = "1234567890";
        String text = "中华人民共和国夏商与西周东周分两段";
        for (String mode : modes) {
            for (String algorithm : algorithms) {
                String transformation = String.format("%s/%s/PKCS5PADDING", algorithm, mode);
                System.out.println("\n-----------------BEGIN " + transformation + "(" + password +  ")----------------");
                try {
                    System.out.format("transformationAlgorithm: %s\n", transformation);

                    String cipherText = SymmetricCiphers.encrypt(transformation, password, text);
                    System.out.format("cipherText: %s\n", cipherText);
                    String plainText = SymmetricCiphers.decrypt(transformation, password, cipherText);
                    System.out.format("plainText: %s\n", plainText);

                } catch (Exception e) {
                    log.info("Ignore algorithm: {}", transformation);
                    log.error("Ignore algorithm: {}", transformation, e);
                } finally {
                    System.out.println("-----------------END " + transformation + "----------------\n");
                }
            }
        }
    }

    @Test
    public void encryptDecrypt() {
        String text = "中华人民共和国夏商与西周东周分两段";
        String[] passwords = { "123", "1234567890abcdef0", "abcdefghijklmnopqrstuwwxyz01234567890"};

        String[] transformations = {
                "DES",
                "DES/CBC/PKCS5PADDING",
                "DES/ECB/PKCS5PADDING",
                "DES/CBC/ISO10126Padding",
                "DES/ECB/ISO10126Padding",
                "DESede",
                "DESede/CBC/PKCS5PADDING",
                "DESede/ECB/PKCS5PADDING",
                "DESede/CBC/ISO10126Padding",
                "DESede/ECB/ISO10126Padding",
                "AES",
                "AES/CBC/PKCS5Padding",
                "AES/ECB/PKCS5Padding",
                "AES/CBC/ISO10126Padding",
                "AES/ECB/ISO10126Padding",
        };

        for (String password : passwords) {
            for (String transformation : transformations) {
                System.out.println("\n-----------------BEGIN " + transformation + "(" + password +  ")----------------");
                try {
                    System.out.format("transformationAlgorithm: %s\n", transformation);

                    String cipherText = SymmetricCiphers.encrypt(transformation, password, text);
                    System.out.format("cipherText: %s\n", cipherText);
                    String plainText = SymmetricCiphers.decrypt(transformation, password, cipherText);
                    System.out.format("plainText: %s\n", plainText);

                } catch (Exception e) {
                    log.info("Ignore algorithm: {}", transformation);
                    log.error("Ignore algorithm: {}", transformation, e);
                } finally {
                    System.out.println("-----------------END " + transformation + "----------------\n");
                }
            }
        }
    }

    private void generateSecretKey(String algorithm, int keysize) {
        SecretKey secretKey = SymmetricCiphers.generateSecretKey(algorithm, keysize);
        byte[] key = secretKey.getEncoded();
        log.info("{}/{}/{}", secretKey.getAlgorithm(), secretKey.getFormat(), key.length);
        log.info("key: {}.", Base64.getEncoder().encodeToString(key));
    }
}
