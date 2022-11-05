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
    private Boolean velocityInput = false;
    private SystemTimer velocityTimer = new SystemTimer();
    private float templateX = 0;
    private float templateY = 0;
    private float templateZ = 0;


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
    @SubscribeEvent
    public void Update(CustomEventHandler.PlayerUpdateEvent event){
        if(FunctionManager.getStatus("Velocity") && !SkyBlockKB()){
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer.hurtTime> 0 && velocityInput) {
            velocityInput = false;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.jumpMovementFactor = -0.002f;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
        }
        if (velocityTimer.hasTimePassed(80L) && velocityInput) {
            velocityInput = false;
            mc.thePlayer.motionX = templateX / 8000.0;
            mc.thePlayer.motionZ = templateZ / 8000.0;
            mc.thePlayer.motionY = templateY / 8000.0;
            mc.thePlayer.jumpMovementFactor = -0.002f;
        }
        }
    }
    @SubscribeEvent
    public void Packet(CustomEventHandler.PacketReceivedEvent event){
        if(FunctionManager.getStatus("Velocity")) {
            if (event.packet instanceof S12PacketEntityVelocity) {
                if(!SkyBlockKB()) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.packet;
                    if (packet.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                        event.setCanceled(true);
                        velocityInput = true;
                        templateX = packet.getMotionX();
                        templateZ = packet.getMotionZ();
                        templateY = packet.getMotionY();
                    }
                }
            }
        }
    }
}
