package com.greencat.antimony.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CachePool<K,V>  {
    int scaling;
    int size;
    private final List<HashMap<K,V>> cache = new ArrayList<HashMap<K, V>>();
    public CachePool(int quantity,int size,int scaling) {
        this.scaling = scaling;
        this.size = size;
        int round = 0;
        while(round < quantity){
            cache.add(new HashMap<K, V>());
            round = round + 1;
        }
    }
    public void put(K key,V value){
        boolean put = false;
        int round = 0;
        while(round < cache.size()){
            HashMap<K, V> map = cache.get(round);
            if(map.containsKey(key)){
                map.put(key, value);
                put = true;
                break;
            } else {
                if (!(map.size() + 1 > (size + ((round * size * scaling))))) {
                    map.put(key, value);
                    put = true;
                    break;
                }
            }
            round = round + 1;
        }
        if(!put){
            clear();
        }
    }
    public V get(K key){
        for(HashMap<K,V> map : cache){
            if(map.containsKey(key)){
                return map.get(key);
            }
        }
        return null;
    }
    public void clear(){
        for(HashMap<K,V> map : cache){
            map.clear();
        }
    }
}
