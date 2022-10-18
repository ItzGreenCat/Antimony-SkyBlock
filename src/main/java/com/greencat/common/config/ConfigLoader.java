package com.greencat.common.config;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.register.AntimonyRegister;
import com.greencat.type.AntimonyFunction;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    private static Configuration config;
    public ConfigLoader(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
    }
    public static void setFunctionStateStorage(){
        List<String> list = new ArrayList<String>();
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            if(function.getStatus()){
                list.add(function.getName());
            }
        }
        config.get(Configuration.CATEGORY_GENERAL, "FunctionStorage",list.toArray(new String[0]), "Function功能开启存储").set(list.toArray(new String[0]));
        config.save();
        config.load();
    }
    public static void applyFunctionState(){
        String[] functions = config.get(Configuration.CATEGORY_GENERAL, "FunctionStorage",new String[0], "Function功能开启存储").getStringList();
        for(String function : functions){
            FunctionManager.setStatus(function,true);
        }
        config.save();
        config.load();
    }
    public static void setChatChannel(boolean status) {
        config.get(Configuration.CATEGORY_GENERAL, "ChatChannel", false, "切换聊天频道").set(status);
        config.save();
        config.load();
    }
    public static boolean getChatChannel() {
        boolean status = config.get(Configuration.CATEGORY_GENERAL, "ChatChannel", false, "切换聊天频道").getBoolean();
        config.save();
        config.load();
        return status;
    }
    //------------------------------------------
    public static void setBoolean(String key,Boolean value,Boolean defaultValue) {
        config.get(Configuration.CATEGORY_GENERAL, key, defaultValue, "").set(value);
        config.save();
        config.load();
    }
    public static boolean getBoolean(String key,Boolean defaultValue) {
        boolean status = config.get(Configuration.CATEGORY_GENERAL,key, defaultValue,"").getBoolean();
        config.save();
        config.load();
        return status;
    }
    public static void setString(String key,String value,String defaultValue) {
        config.get(Configuration.CATEGORY_GENERAL, key, defaultValue, "").set(value);
        config.save();
        config.load();
    }
    public static String getString(String key,String defaultValue) {
        String str = config.get(Configuration.CATEGORY_GENERAL,key, defaultValue,"").getString();
        config.save();
        config.load();
        return str;
    }
    public static void setInt(String key,int value,int defaultValue) {
        config.get(Configuration.CATEGORY_GENERAL, key, defaultValue, "").set(value);
        config.save();
        config.load();
    }
    public static int getInt(String key,int defaultValue) {
        int number = config.get(Configuration.CATEGORY_GENERAL,key, defaultValue,"").getInt();
        config.save();
        config.load();
        return number;
    }
    public static void setDouble(String key,double value,double defaultValue) {
        config.get(Configuration.CATEGORY_GENERAL, key, defaultValue, "").set(value);
        config.save();
        config.load();
    }
    public static double getDouble(String key,double defaultValue) {
        double number = config.get(Configuration.CATEGORY_GENERAL,key, defaultValue,"").getDouble();
        config.save();
        config.load();
        return number;
    }
}
