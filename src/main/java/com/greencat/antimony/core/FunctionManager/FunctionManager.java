package com.greencat.antimony.core.FunctionManager;

import com.greencat.Antimony;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.ui.FunctionNotice;
import com.greencat.antimony.core.settings.AbstractSettingOptionButton;
import com.greencat.antimony.core.settings.AbstractSettingOptionTextField;
import com.greencat.antimony.core.settings.ISettingOption;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class FunctionManager {
    //The function that is currently bound
    private static String currentFunction = "";
    //get a function's status
    public static Boolean getStatus(String Name){
        /*Optional<AntimonyFunction> optional = AntimonyRegister.FunctionList.stream().filter(it -> it.getName().equals(Name)).findFirst();
        if(optional.isPresent()){
            return optional.get().getStatus();
        } else {
            return false;
        }*/
        return AntimonyRegister.FunctionList != null ? AntimonyRegister.FunctionList.get(Name) != null ? AntimonyRegister.FunctionList.get(Name).getStatus() : false : false;
        /*for(int i = 0;i < AntimonyRegister.FunctionList.size();++i){
            if(AntimonyRegister.FunctionList.get(i).getName().equals(Name)){

            }
        }*/
        /*for(AntimonyFunction function : AntimonyRegister.FunctionList){

        }*/
    }
    //set a funtion's status
    public static void setStatus(String Name,Boolean status){
        modifyCount++;
        AntimonyFunction function = AntimonyRegister.FunctionList.get(Name);
                if(status){
                    CustomEventHandler.FunctionEnableEvent event;
                    event = new CustomEventHandler.FunctionEnableEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.setStatus(true);
                        FunctionNotice.ShowNotice(function.getName(),function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        Utils.print(Name + EnumChatFormatting.WHITE + " 启用");
                    }
                }
                if(!status){
                    CustomEventHandler.FunctionDisabledEvent event;
                    event = new CustomEventHandler.FunctionDisabledEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.setStatus(false);
                        FunctionNotice.ShowNotice(function.getName(),function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        Utils.print(Name + EnumChatFormatting.WHITE + " 禁用");
                    }
                }
                AntimonyRegister.reloadEnableList();
    }
    //switch a function's status without any notice
    public static void switchStatusNoNotice(String Name) {
        modifyCount++;
        AntimonyFunction function = AntimonyRegister.FunctionList.get(Name);
                CustomEventHandler.FunctionSwitchEvent event;
                event = new CustomEventHandler.FunctionSwitchEvent(function, !function.getStatus());
                CustomEventHandler.EVENT_BUS.post(event);
                if (!event.isCanceled()) {
                    //function.color = new Random().nextInt(4);
                    function.SwtichStatus();
                    ConfigLoader.setFunctionStateStorage();
                }
        AntimonyRegister.reloadEnableList();
    }
    //set a function's status without any notice
    public static void setStatusNoNotice(String Name,Boolean status){
        modifyCount++;
        AntimonyFunction function = AntimonyRegister.FunctionList.get(Name);
                if(status){
                    CustomEventHandler.FunctionEnableEvent event;
                    event = new CustomEventHandler.FunctionEnableEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.setStatus(true);
                        ConfigLoader.setFunctionStateStorage();
                    }
                }
                if(!status){
                    CustomEventHandler.FunctionDisabledEvent event;
                    event = new CustomEventHandler.FunctionDisabledEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.setStatus(false);
                        ConfigLoader.setFunctionStateStorage();
                    }
                }
        AntimonyRegister.reloadEnableList();
    }
    //switch a function's status
    public static void switchStatus(String Name){
        modifyCount++;
        AntimonyFunction function = AntimonyRegister.FunctionList.get(Name);
                    CustomEventHandler.FunctionSwitchEvent event;
                    event = new CustomEventHandler.FunctionSwitchEvent(function,!function.getStatus());
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.SwtichStatus();
                        if(!Name.equals("Pathfinding")) {
                            FunctionNotice.ShowNoticeSound(Name, function.getStatus());
                        } else {
                            FunctionNotice.ShowNotice(Name,function.getStatus());
                        }
                        ConfigLoader.setFunctionStateStorage();
                        if(function.getStatus()){
                            Utils.print(Name + EnumChatFormatting.WHITE + " 启用");
                        } else {
                            Utils.print(Name + EnumChatFormatting.WHITE + " 禁用");
                        }
                    }
        AntimonyRegister.reloadEnableList();
    }
    static int width = 0;
    static int prevModifyCount = 0;
    static int modifyCount = 0;
    public static int getLongestTextWidth(){
        int width = 0;
        for (Map.Entry<String,AntimonyFunction> entry : AntimonyRegister.FunctionList.entrySet()) {
            AntimonyFunction function = entry.getValue();
            width = Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(function.getName()),width);
        }
        return width;
    }
    //get the longest function name and add 20 more
    public static int getLongestTextWidthAdd20(){
        if(modifyCount != prevModifyCount){
            width = getLongestTextWidth();
            prevModifyCount = modifyCount;
        }
        return width + 20;
    }
    //get the longest function name and add 5 more
    public static int getLongestTextWidthAdd5(){
        if(modifyCount != prevModifyCount){
            width = getLongestTextWidth();
            prevModifyCount = modifyCount;
        }
        return width + 5;
    }
    //use function name to get a AntimonyFunction
    public static AntimonyFunction getFunctionByName(String name){
        return AntimonyRegister.FunctionList.get(name);
    }
    //bind a function
    public static void bindFunction(String Name){
        currentFunction = Name;
    }
    //add a config to bound function
    public static void addConfiguration(ISettingOption option){
        try {
            if(option instanceof AbstractSettingOptionButton) {
                ((AbstractSettingOptionButton)option).parentFunction = currentFunction;
                FunctionManager.getFunctionByName(currentFunction).addConfigurationOption(option);
                Antimony.LOGGER.info("Register Config -> type: " + option.getClass().getSimpleName() + " id: " + ((AbstractSettingOptionButton) option).ID);
            } else if(option instanceof AbstractSettingOptionTextField){
                ((AbstractSettingOptionTextField)option).parentFunction = currentFunction;
                FunctionManager.getFunctionByName(currentFunction).addConfigurationOption(option);
                Antimony.LOGGER.info("Register Config -> type: " + option.getClass().getSimpleName() + " id: " + ((AbstractSettingOptionTextField) option).ID);
            }
        } catch(NullPointerException e){
            System.err.println("[Antimony] Unable to add Configuration " + option + " to " + currentFunction + ".");
        }
    }
}
