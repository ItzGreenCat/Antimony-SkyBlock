package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.key.KeyLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class Velocity {
    static final String location = "Velocity功能位置在MixinNetHandlerPlayClient.class,当前位置仅用于存储物品列表";

    public static List<String> BootsIDList = new ArrayList<String>();
    public static List<String> ItemIDList = new ArrayList<String>();

    public Velocity(){
        BootsIDList.add("SPIDER_BOOTS");
        BootsIDList.add("TARANTULA_BOOTS");

        ItemIDList.add("BONZO_STAFF");
        ItemIDList.add("STARRED_BONZO_STAFF");
        ItemIDList.add("JERRY_STAFF");
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void InputEvent(InputEvent.KeyInputEvent event){
        if(KeyLoader.SwitchVelocity.isPressed()){
            FunctionManager.switchStatus("Velocity");
        }
    }
}
