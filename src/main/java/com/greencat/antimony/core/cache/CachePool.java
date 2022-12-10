package com.greencat.antimony.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//the cache pool based on HashMap
public class CachePool<K,V>  {
    //Multipliers between different tiers of cache
    int scaling;
    //The size of the first-level cache
    int size;
    private final List<HashMap<K,V>> cache = new ArrayList<HashMap<K, V>>();
    /*
    * init a cache pool
    * @author 绿猫GreenCat
    * @param quantity The number of layers of the cache
    * @param size size of the first-level cache
    * @param scaling Multipliers between different tiers of cache
    * */
    public CachePool(int quantity,int size,int scaling) {
        this.scaling = scaling;
        this.size = size;
        int round = 0;
        while(round < quantity){
            cache.add(new HashMap<K, V>());
            round = round + 1;
        }
    }
    //put the key and value to the cache
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
    //get value in cache by key
    public V get(K key){
        for(HashMap<K,V> map : cache){
            if(map.containsKey(key)){
                return map.get(key);
            }
        }
        return null;
    }
    //clear cache
    public void clear(){
        for(HashMap<K,V> map : cache){
            map.clear();
        }
    }
}
