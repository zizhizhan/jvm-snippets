package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
public class CryptographyTests {

    @Test
    public void secureRandom() {
        SecureRandom random = new SecureRandom();
        log.info("{} with provider {}.", random.getAlgorithm(), random.getProvider());
        String origin = "1234567890";
        byte[] bytes = origin.getBytes(StandardCharsets.UTF_8);
        random.nextBytes(bytes);
        System.out.println(Arrays.toString(bytes));

        bytes = new byte[32];
        random.nextBytes(bytes);
        System.out.println(Arrays.toString(bytes));

        System.out.println(Arrays.toString(random.generateSeed(16)));
        System.out.println(Arrays.toString(SecureRandom.getSeed(16)));

        String password = "password";
        for (int i = 0; i < 3; i++) {
            SecureRandom seedRandom = new SecureRandom(password.getBytes(StandardCharsets.UTF_8));

            byte[] bytes1 = new byte[2];
            seedRandom.nextBytes(bytes1);
            System.out.println(Arrays.toString(bytes1));

            byte[] bytes2 = new byte[8];
            seedRandom.nextBytes(bytes2);
            System.out.println(Arrays.toString(bytes2));
        }

    }
}
