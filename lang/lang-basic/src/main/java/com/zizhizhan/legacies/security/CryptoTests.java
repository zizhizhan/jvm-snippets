package com.zizhizhan.legacies.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTests {

    public static void main(String[] args) {
        for (String algorithmName : getAlgorithms()) {
            test(algorithmName);
        }
    }

    public static void test(String algorithmName) {
        try {
            System.out.println("Getting key generator ...");
            KeyGenerator kgen = KeyGenerator.getInstance(algorithmName);
            System.out.println("Generating key ...");

            SecretKey secretKey = kgen.generateKey();
            byte[] bytes = secretKey.getEncoded();
            System.out.format("\nSecrete key: %s, %s\n", new String(bytes, "iso8859-1"), secretKey.getFormat());
            SecretKeySpec keySpec = new SecretKeySpec(bytes, algorithmName);

            System.out.println("Creating cipher ... ");
            Cipher cipher = Cipher.getInstance(algorithmName);

            System.out.println("Encrypting ... ");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            String target = "Encrypt this buddy.";
            byte[] encrypted = cipher.doFinal(target.getBytes("iso8859-1"));

            System.out.println("Before: " + target);
            System.out.println("After: " + new String(encrypted, "iso8859-1"));

            //Decrypt
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            System.out.format("\nafter decrypt: %s \n\n\n", new String(decrypted, "iso8859-1"));

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static String[] getAlgorithms() {
        return new String[]{"Blowfish", "DESede/CBC/PKCS5Padding"};
    }


}
