package com.zizhizhan.jsr107.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class EhcacheMain {

    public static void main(String[] args) {
        CacheConfiguration<String, String> config = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(100))
                .build();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured", config)
                .build(true);

        Cache<String, String> preConfigured = cacheManager.getCache("preConfigured", String.class, String.class);

        Cache<String, String> myCache = cacheManager.createCache("myCache", config);

        myCache.put("one", "one one!");
        String value = myCache.get("one");
        System.out.println(value);

        preConfigured.put("two", "two two");
        System.out.println(preConfigured.get("two"));

        cacheManager.close();
    }

}
