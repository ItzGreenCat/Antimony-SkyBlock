package com.GreenCat.common.config;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.common.register.GCARegister;
import com.GreenCat.type.AntimonyFunction;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    static int Height;
    static int PetLevel;
    private static Configuration config;
    public ConfigLoader(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
    }
    public static int GuiHeight() {
        Height = config.get(Configuration.CATEGORY_GENERAL, "SelectGuiHeight", 0, "SelectGUI距离屏幕顶部高度").getInt();
        config.save();
        config.load();
        return Height;
    }
    public static int FunctionListHeight() {
        Height = config.get(Configuration.CATEGORY_GENERAL, "FunctionGuiHeight", 0, "FunctionGUI距离屏幕顶部高度").getInt();
        config.save();
        config.load();
        return Height;
    }
    public static void SetGuiHeight(int height){
        config.get(Configuration.CATEGORY_GENERAL, "SelectGuiHeight", 0, "SelectGUI距离屏幕顶部高度").set(height);
        config.save();
        config.load();
    }
    public static void SetFunctionGuiHeight(int height){
        config.get(Configuration.CATEGORY_GENERAL, "FunctionGuiHeight", 0, "FunctionGUI距离屏幕顶部高度").set(height);
        config.save();
        config.load();
    }
    public static void setFunctionStateStorage(){
        List<String> list = new ArrayList<String>();
        for(AntimonyFunction function : GCARegister.FunctionList){
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
    public static String[] getPetNameRule(){
        String[] functions = config.get(Configuration.CATEGORY_GENERAL, "PetNameRuleStorage",new String[0], "pet名称规则").getStringList();
        config.save();
        config.load();
        return functions;
    }
    public static void setPetNameRule(String Rules){
        if(!Rules.equals("null")){
            String[] RulesArray = Rules.split(",");
            config.get(Configuration.CATEGORY_GENERAL, "PetNameRuleStorage",RulesArray, "pet名称规则").set(RulesArray);
        } else {
            config.get(Configuration.CATEGORY_GENERAL, "PetNameRuleStorage",new String[0], "pet名称规则").set(new String[0]);
        }
        config.save();
        config.load();
    }
    public static void setCustomPetLevel(int level) {
        config.get(Configuration.CATEGORY_GENERAL, "CustomPetLevel", 0, "pet等级").set(level);
        config.save();
        config.load();
    }
    public static int getCustomPetLevel() {
        PetLevel = config.get(Configuration.CATEGORY_GENERAL, "CustomPetLevel", 0, "pet等级").getInt();
        config.save();
        config.load();
        return PetLevel;
    }
}
