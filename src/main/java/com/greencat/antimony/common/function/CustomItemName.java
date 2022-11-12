package com.greencat.antimony.common.function;

import java.util.HashMap;
import java.util.Map;

public class CustomItemName {
    public static HashMap<String,String> CustomName = new HashMap<String,String>();
    public static Boolean hasCustomName(String UUID){
        boolean hasCustomName = false;
        for(Map.Entry<String,String> entry : CustomName.entrySet()){
            if(entry.getKey().equals(UUID)){
                hasCustomName = true;
            }
        }
        return hasCustomName;
    }
    public static String getCustomName(String UUID) {
        for(Map.Entry<String,String> entry : CustomName.entrySet()){
            if(entry.getKey().equals(UUID)){
                String finalName = entry.getValue().replace("&","§");
                return finalName;
            }
        }
        return "";
    }
}
