package com.greencat.common.config;

import com.greencat.common.exceptions.NoSuchConfigurationException;
import com.greencat.common.register.AntimonyRegister;
import com.greencat.type.AntimonyFunction;

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
