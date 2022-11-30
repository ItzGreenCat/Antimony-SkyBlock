package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.key.KeyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public Boolean SkyBlockKB(){
        if(!Minecraft.getMinecraft().thePlayer.isInLava()) {
            ItemStack held = Minecraft.getMinecraft().thePlayer.getHeldItem();
            return held != null && (held.getDisplayName().contains("Bonzo's Staff") || held.getDisplayName().contains("Jerry-chine Gun"));
        }
        return true;
    }
}
