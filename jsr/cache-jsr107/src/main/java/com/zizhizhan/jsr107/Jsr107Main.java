package com.zizhizhan.jsr107;

import org.jsr107.ri.spi.RICachingProvider;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

public class Jsr107Main {

    public static void main(String[] args) {
        // CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        CacheManager cacheManager = Caching.getCachingProvider(RICachingProvider.class.getName()).getCacheManager();

        MutableConfiguration<String, String> config = new MutableConfiguration<String, String>()
                .setTypes(String.class, String.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
                .setStatisticsEnabled(true);

        Cache<String, String> cache = cacheManager.createCache("testCache", config);

        String key = "testKey";
        cache.put(key, "testValue");
        System.out.println(cache.get(key));
        cache.remove(key);
        System.out.println(cache.get(key));
    }


}
