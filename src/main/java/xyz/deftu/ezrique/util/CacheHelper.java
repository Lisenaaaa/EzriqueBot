package xyz.deftu.ezrique.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheHelper {

    public static <K, V> Cache<K, V> createCache(int duration, TimeUnit unit) {
        return Caffeine.newBuilder().expireAfterWrite(duration, unit).build();
    }

    public static <K, V> Cache<K, V> createCache() {
        return createCache(30, TimeUnit.SECONDS);
    }

}