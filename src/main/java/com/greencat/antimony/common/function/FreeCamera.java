package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeCamera {
    private EntityOtherPlayerMP playerEntity;

    public FreeCamera() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if(event.function.getName().equals("FreeCamera")) {
            if(Minecraft.getMinecraft().theWorld != null) {
                this.playerEntity = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
                this.playerEntity.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
                this.playerEntity.onGround = Minecraft.getMinecraft().thePlayer.onGround;
                Minecraft.getMinecraft().theWorld.addEntityToWorld(-114514, this.playerEntity);
            }
        }
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("FreeCamera")) {
            if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && this.playerEntity != null) {
                Minecraft.getMinecraft().thePlayer.noClip = false;
                Minecraft.getMinecraft().thePlayer.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
                Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-114514);
                this.playerEntity = null;
                Minecraft.getMinecraft().thePlayer.setVelocity(0.0D, 0.0D, 0.0D);
            }
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("FreeCamera")){
            if(event.status){
                if(Minecraft.getMinecraft().theWorld != null) {
                    this.playerEntity = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
                    this.playerEntity.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
                    this.playerEntity.onGround = Minecraft.getMinecraft().thePlayer.onGround;
                    Minecraft.getMinecraft().theWorld.addEntityToWorld(-114514, this.playerEntity);
                }
            }
            if(!event.status){
                if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && this.playerEntity != null) {
                    Minecraft.getMinecraft().thePlayer.noClip = false;
                    Minecraft.getMinecraft().thePlayer.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
                    Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-114514);
                    this.playerEntity = null;
                    Minecraft.getMinecraft().thePlayer.setVelocity(0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (FunctionManager.getStatus("FreeCamera")){
            Minecraft.getMinecraft().thePlayer.noClip = true;
            Minecraft.getMinecraft().thePlayer.fallDistance = 0.0F;
            Minecraft.getMinecraft().thePlayer.onGround = false;
            Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
            Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
            if (!Utils.isMoving()) {
                Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
                Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
            }

            double speed = 0.3D;
            Minecraft.getMinecraft().thePlayer.jumpMovementFactor = (float)speed;
            EntityPlayerSP entityFakePlayer1919810;
            if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
                entityFakePlayer1919810 = Minecraft.getMinecraft().thePlayer;
                entityFakePlayer1919810.motionY += speed * 3.0D;
            }

            if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
                entityFakePlayer1919810 = Minecraft.getMinecraft().thePlayer;
                entityFakePlayer1919810.motionY -= speed * 3.0D;
            }
        }

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        if (FunctionManager.getStatus("FreeCamera")){
            FunctionManager.setStatus("FreeCamera", false);
        }

    }

    @SubscribeEvent
    public void onPacket(CustomEventHandler.PacketSentEvent event) {
        if (FunctionManager.getStatus("FreeCamera") && event.packet instanceof C03PacketPlayer) {
            event.setCanceled(true);
            Utils.sendPacketNoEvent(new C03PacketPlayer(this.playerEntity.onGround));
        }

    }
}
