package xyz.deftu.ezrique.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheHelper {

    public static <K, V> Caffeine<K, V> create(int duration, TimeUnit unit) {
        return (Caffeine<K, V>) Caffeine.newBuilder().expireAfterWrite(duration, unit);
    }

    public static <K, V> Caffeine<K, V> create() {
        return create(30, TimeUnit.SECONDS);
    }

    public static <K, V> Cache<K, V> createCache(int duration, TimeUnit unit) {
        return create(duration, unit).build();
    }

    public static <K, V> Cache<K, V> createCache() {
        return createCache(30, TimeUnit.SECONDS);
    }

}