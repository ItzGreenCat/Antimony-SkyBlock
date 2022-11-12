package com.greencat.antimony.core.config;

import com.greencat.antimony.core.exceptions.NoSuchConfigurationException;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;

public class getConfigByFunctionName {
    public static Object get(String FunctionName,String ConfigID){
        for(AntimonyFunction function : AntimonyRegister.FunctionList) {
            if(function.getName().equals(FunctionName)){
                try {
                    return function.getConfigurationValue(ConfigID);
                } catch(NoSuchConfigurationException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
