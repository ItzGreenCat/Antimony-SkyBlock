package com.greencat.common.function;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.key.KeyLoader;
import com.greencat.common.mixins.S12Accessor;
import com.greencat.utils.Utils;
import com.greencat.utils.timer.SystemTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

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
    @SubscribeEvent
    public void InputEvent(InputEvent.KeyInputEvent event){
        if(KeyLoader.SwitchVelocity.isPressed()){
            FunctionManager.switchStatus("Velocity");
        }
    }
}
