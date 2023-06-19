package com.greencat.antimony.core.config;

import com.greencat.antimony.core.cache.CachePool;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;

public class ConfigInterface {
    public static CachePool<String,Object> cache;
    public ConfigInterface(){
        //Instantiate a cache pool
        cache = new CachePool<>(3, 7, 5);
    }
    //get a config value by function name
    //will get it at cache first, if cannot find in cache,then will get it at config file and put it into cache pool
    public static Object get(String FunctionName,String ConfigID){
        Object value;
        value = cache.get(FunctionName + "_" + ConfigID);
        if(value == null){
            AntimonyFunction function = AntimonyRegister.FunctionList.get(FunctionName);
            value = function.getConfigurationValue(ConfigID);
            if(value != null) {
                cache.put(FunctionName + "_" + ConfigID, value);
            }
        }
        if(FunctionName.equals("FPS Accelerator") && ConfigID.equals("fmlLog") && value == null){
            return false;
        }
        return value;
    }
}
