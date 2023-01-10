package com.greencat.antimony.core.FunctionManager;

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

import java.util.Random;

public class FunctionManager {
    //The function that is currently bound
    private static String currentFunction = "";
    //get a function's status
    public static Boolean getStatus(String Name){
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            if(function.getName().equals(Name)){
                return function.getStatus();
            }
        }
        return false;
    }
    //set a funtion's status
    public static void setStatus(String Name,Boolean status){
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            if(function.getName().equals(Name)){
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
                break;
            }
        }
    }
    //switch a function's status without any notice
    public static void switchStatusNoNotice(String Name) {
        
        for (AntimonyFunction function : AntimonyRegister.FunctionList) {
            if (function.getName().equals(Name)) {


                CustomEventHandler.FunctionSwitchEvent event;
                event = new CustomEventHandler.FunctionSwitchEvent(function, !function.getStatus());
                CustomEventHandler.EVENT_BUS.post(event);
                if (!event.isCanceled()) {
                    //function.color = new Random().nextInt(4);
                    function.SwtichStatus();
                    ConfigLoader.setFunctionStateStorage();
                }
                break;

            }
        }
    }
    //set a function's status without any notice
    public static void setStatusNoNotice(String Name,Boolean status){
        
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            if(function.getName().equals(Name)){
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
                break;
            }
        }
    }
    //switch a function's status
    public static void switchStatus(String Name){
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            if(function.getName().equals(Name)){
                    CustomEventHandler.FunctionSwitchEvent event;
                    event = new CustomEventHandler.FunctionSwitchEvent(function,!function.getStatus());
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        //function.color = new Random().nextInt(4);
                        function.SwtichStatus();
                        FunctionNotice.ShowNoticeSound(Name, function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        if(function.getStatus()){
                            Utils.print(Name + EnumChatFormatting.WHITE + " 启用");
                        } else {
                            Utils.print(Name + EnumChatFormatting.WHITE + " 禁用");
                        }
                    }
                break;
            }
        }
    }
    //get the longest function name and add 20 more
    public static int getLongestTextWidthAdd20(){
        int width = 0;
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
           width = Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(function.getName()),width);
        }
        return width + 20;
    }
    //get the longest function name and add 5 more
    public static int getLongestTextWidthAdd5(){
        int width = 0;
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            width = Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(function.getName()),width);
        }
        return width + 5;
    }
    //use function name to get a AntimonyFunction
    public static AntimonyFunction getFunctionByName(String name){
        for(AntimonyFunction function : AntimonyRegister.FunctionList) {
            if(function.getName().equals(name)){
                return function;
            }
        }
        return null;
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
            } else if(option instanceof AbstractSettingOptionTextField){
                ((AbstractSettingOptionTextField)option).parentFunction = currentFunction;
                FunctionManager.getFunctionByName(currentFunction).addConfigurationOption(option);
            }
        } catch(NullPointerException e){
            System.err.println("[Antimony] Unable to add Configuration " + option + " to " + currentFunction + ".");
        }
    }
}
