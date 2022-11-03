package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.type.CheckableValue;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import scala.tools.nsc.typechecker.Checkable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTranslate {
    static HashMap<String,String> cacheMap = new HashMap<>();
    public static HashMap<String,String> HighLevelMapping = new HashMap<>();
    public static HashMap<String,String> normalMapping = new HashMap<>();

    public ItemTranslate(){
        HighLevelMapping = Utils.getHashMapInJsonFile("/HighLevelMapping.json");

        normalMapping = Utils.getHashMapInJsonFile("/NormalMapping.json");
    }
    public String modifyName(String CustomName){
        if(FunctionManager.getStatus("ItemTranslate")) {
            CheckableValue checkableValue = getNameByCache(CustomName);
            if(!checkableValue.check) {
                String modifiedName = CustomName;
                for (Map.Entry<String,String> nameMapping : HighLevelMapping.entrySet()) {
                    modifiedName = modifiedName.replace(nameMapping.getKey(), nameMapping.getValue());
                }
                for (Map.Entry<String,String> nameMapping : normalMapping.entrySet()) {
                    modifiedName = modifiedName.replace(nameMapping.getKey(), nameMapping.getValue());
                }
                if(cacheMap.size() + 1 > 30){
                    cacheMap.clear();
                }
                cacheMap.put(CustomName,modifiedName);
                return modifiedName;
            } else {
                return checkableValue.value;
            }
        }
        return CustomName;
    }
    private CheckableValue getNameByCache(String originalName){
        boolean findValue = false;
        String value = null;
        if(!cacheMap.isEmpty()) {
            for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
                if (entry.getKey().equals(originalName)) {
                    value = entry.getValue();
                    findValue = true;
                    return new CheckableValue(value, findValue);
                }
            }
        }
        return new CheckableValue(value,findValue);
    }
}
