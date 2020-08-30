package com.zizhizhan.jsr107.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CaffeineMain {

    public static void main(String[] args) {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(key -> UUID.randomUUID().toString());

        System.out.println(loadingCache.get("a"));
        System.out.println(loadingCache.get("b"));
        System.out.println(loadingCache.stats());
        System.out.println(loadingCache.get("a"));
        System.out.println(loadingCache.get("b"));
    }

}
