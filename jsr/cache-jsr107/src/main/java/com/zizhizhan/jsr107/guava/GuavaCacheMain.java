package com.zizhizhan.jsr107.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaCacheMain {

    private static final LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .removalListener(n -> {
                log.info("[RemoveItem] {} with {} remove by {}.", n.getKey(), n.getValue(), n.getCause());
            })
            .build(new CacheLoader<String, String>() {
                public String load(@Nonnull String key) {
                    log.info("[Create] Not found {}, create it!", key);
                    return generateCacheValue(key);
                }
            });

    private static final Random random = new Random();

    public static void main(String[] args) {
        String[] testKeys = {"hello", "world", "a", "b", "c", "x", "y", "z", "helloworld"};
        int count = 0;
        while (count < 100) {
            String key = testKeys[random.nextInt(testKeys.length)];
            try {
                System.out.format("[GET] %s: %s.\n", key, localCache.get(key));
            } catch (Exception e) {
                log.warn("{} with {}.", key, e.getMessage(), e);
            }

            key = testKeys[random.nextInt(testKeys.length)];
            try {
                System.out.format("[GETUNNECKED] %s: %s.\n", key, localCache.getUnchecked(key));
            } catch (Exception e) {
                log.warn("{} with {}.", key, e.getMessage(), e);
            }

            key = testKeys[random.nextInt(testKeys.length)];
            String valuePresent = localCache.getIfPresent(key);
            System.out.format("[GETIFPRESENT] %s: %s.\n", key, valuePresent);
            if (valuePresent != null) {
                log.info("Invalidate {} with value {}.", key, valuePresent);
                localCache.invalidate(key);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.warn("Unexpected interrupt.", e);
            }
            count++;
        }
    }

    private static String generateCacheValue(String key) {
        long seed = System.currentTimeMillis();
        if (seed % 8 == 0) {
            throw new IllegalStateException("Unexpected key " + key + " with seed " + seed + ".");
        } else {
            return sha256(key);
        }
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // bytes to hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
            return input;
        }
    }
}
