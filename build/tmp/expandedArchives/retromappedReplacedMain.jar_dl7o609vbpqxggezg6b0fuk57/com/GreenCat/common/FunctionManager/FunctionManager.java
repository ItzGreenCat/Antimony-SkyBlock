package com.GreenCat.common.FunctionManager;

import com.GreenCat.common.config.ConfigLoader;
import com.GreenCat.common.event.CustomEventHandler;
import com.GreenCat.common.register.GCARegister;
import com.GreenCat.common.ui.FunctionNotice;
import com.GreenCat.type.AntimonyFunction;
import com.GreenCat.utils.Utils;
import net.minecraft.util.EnumChatFormatting;

import java.util.Random;

public class FunctionManager {
    public static Boolean getStatus(String Name){
        for(AntimonyFunction function : GCARegister.FunctionList){
            if(function.getName().equals(Name)){
                return function.getStatus();
            }
        }
        return false;
    }
    public static void setStatus(String Name,Boolean status){
        Utils utils = new Utils();
        for(AntimonyFunction function : GCARegister.FunctionList){
            if(function.getName().equals(Name)){
                if(status){
                    CustomEventHandler.FunctionEnableEvent event;
                    event = new CustomEventHandler.FunctionEnableEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        function.color = new Random().nextInt(4);
                        function.setStatus(true);
                        FunctionNotice notice = new FunctionNotice();
                        notice.ShowNotice(function.getName(),function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        utils.print(Name + EnumChatFormatting.WHITE + " 启用");
                    }
                }
                if(!status){
                    CustomEventHandler.FunctionDisabledEvent event;
                    event = new CustomEventHandler.FunctionDisabledEvent(function);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        function.color = new Random().nextInt(4);
                        function.setStatus(false);
                        FunctionNotice notice = new FunctionNotice();
                        notice.ShowNotice(function.getName(),function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        utils.print(Name + EnumChatFormatting.WHITE + " 禁用");
                    }
                }
            }
        }
    }
    public static void switchStatus(String Name){
        Utils utils = new Utils();
        for(AntimonyFunction function : GCARegister.FunctionList){
            if(function.getName().equals(Name)){


                    CustomEventHandler.FunctionSwitchEvent event;
                    event = new CustomEventHandler.FunctionSwitchEvent(function,!function.getStatus());
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(!event.isCanceled()){
                        function.color = new Random().nextInt(4);
                        function.SwtichStatus();
                        FunctionNotice notice = new FunctionNotice();
                        notice.ShowNotice(Name, function.getStatus());
                        ConfigLoader.setFunctionStateStorage();
                        if(function.getStatus()){
                            utils.print(Name + EnumChatFormatting.WHITE + " 启用");
                        } else {
                            utils.print(Name + EnumChatFormatting.WHITE + " 禁用");
                        }
                    }

            }
        }
    }
}
