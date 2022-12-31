package com.greencat.antimony.core.config;

import com.greencat.antimony.core.cache.CachePool;
import com.greencat.antimony.core.exceptions.NoSuchConfigurationException;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;

public class getConfigByFunctionName {
    public static CachePool<String,Object> cache;
    public getConfigByFunctionName(){
        //Instantiate a cache pool
        cache = new CachePool<>(5, 20, 2);
    }
    //get a config value by function name
    //will get it at cache first, if cannot find in cache,then will get it at config file and put it into cache pool
    public static Object get(String FunctionName,String ConfigID){
        Object value;
        value = cache.get(FunctionName + "_" + ConfigID);
        if(value == null){
            for (AntimonyFunction function : AntimonyRegister.FunctionList) {
                if (function.getName().equals(FunctionName)) {
                    try {
                        value = function.getConfigurationValue(ConfigID);
                        if(value != null) {
                            cache.put(FunctionName + "_" + ConfigID, value);
                        }
                    } catch (NoSuchConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return value;
    }
}
