package com.zizhizhan.legacies.crypto;

import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

@Slf4j
public class CipherFuncTests {

    public static void main(String[] args) {
        testIdentifyProviders();
        testDes();
    }

    public static void testIdentifyProviders() {
        tryProvider("DESede/ECB/PKCS5Padding", "SunJCE");
        tryProvider("DES/CBC/PKCS5Padding", "SunJCE");
        tryProvider("RSA", "SunJCE");
        tryProvider("RSA/ECB/PKCS1Padding", "SunJCE");
        tryProvider("DESede/ECB/PKCS5Padding", "SunMSCAPI");
        tryProvider("RSA", "SunMSCAPI");
        tryProvider("RSA/ECB/PKCS1Padding", "SunMSCAPI");
    }

    private static void tryProvider(String cipherName, String provider) {
        try {
            Cipher.getInstance(cipherName, provider);
        } catch (Exception e) {
            log.info("Failed " + provider + " (" + cipherName + ") = " + e.getMessage());
            return;
        }
        log.info("Found " + provider + " (" + cipherName + ")");
    }

    private static void testDes() {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            Key key = getKey("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] data = "中国加油， 加油中国".getBytes();
            byte[] raw = cipher.doFinal(data);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(raw);

            System.out.println(new String(raw));
            System.out.println(new String(data));
            System.out.println(new String(result));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Key getKey(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(new SecureRandom());
        return generator.generateKey();
    }
}
