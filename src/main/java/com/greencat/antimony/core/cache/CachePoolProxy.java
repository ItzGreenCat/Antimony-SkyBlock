package com.greencat.antimony.core.cache;
import java.util.function.Supplier;

public class CachePoolProxy<K,V> {
    private final CachePool<K,V> cachePool;
    public CachePoolProxy(int quantity,int size,int scaling) {
        cachePool = new CachePool<K, V>(quantity,size,scaling);
    }
    public V get(K key, Supplier<V> supplier){
        V value = cachePool.get(key);
        if(value == null){
            value = supplier.get();
            cachePool.put(key, value);
        }
        return value;
    }
}
