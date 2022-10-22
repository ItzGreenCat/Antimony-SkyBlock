package com.greencat.common.function;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class AutoFish {
    public void AutoFishEventRegiser(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    static int Tick = 40;
    static int hookTick = -1;
    static int maxHookTick = 1400;
    static boolean MoveStatus = false;
    static Boolean AutoFishStatus = false;
    static Boolean slugFish = false;


    public AutoFish() throws AWTException {
        try{
            if (FunctionManager.getStatus("AutoFish")) {
                if (mc.thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    this.Tick = 0;
                    //utils.print("Fish now Up");
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void StartTriggerAutoFish(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!FunctionManager.getStatus("AutoFish")){
                AutoFishStatus = false;
            }
        }
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld != null) {
            if (Tick < 40) {
                if (Tick == 0) {
                    if(Antimony.AutoFishYawState) {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw + 0.3);
                        mc.thePlayer.rotationPitch = (float) (mc.thePlayer.rotationPitch + 0.3);
                        Antimony.AutoFishYawState = false;
                    } else {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw - 0.3);
                        mc.thePlayer.rotationPitch = (float) (mc.thePlayer.rotationPitch - 0.3);
                        Antimony.AutoFishYawState = true;
                    }
                    try {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),true);
                } else if (Tick == 1) {
                    try {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (Tick == 29) {
                    if(MoveStatus){
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),true);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),true);
                    }
                } else if(Tick == 30){
                    if(MoveStatus){
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),true);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),true);
                    }
                } else if(Tick == 32){
                    if(MoveStatus){
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),false);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),false);
                    }
                    MoveStatus = !MoveStatus;
                } else if (Tick == 39) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),false);
                }
                Tick = Tick + 1;
            } else {
                Tick = 40;
            }
        }
        if(hookTick + 1 >= maxHookTick){
            hookTick = -1;
        } else {
            if(hookTick + 1 < maxHookTick && hookTick >= 0){
                hookTick = hookTick + 1;
            }
        }
    }
    @SubscribeEvent
    public void onPacketReceived(PlaySoundEvent event) throws AWTException {
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoFish")) {
                if (event.name.equals("game.player.swim.splash")) {
                    if (AutoFishStatus) {
                        float x = event.result.getXPosF();
                        float y = event.result.getYPosF();
                        float z = event.result.getZPosF();
                        List<EntityFishHook> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityFishHook.class, new AxisAlignedBB(x - (0.5 / 2d), y - (0.5 / 2d), z - (0.5 / 2d), x + (0.5 / 2d), y + (0.5 / 2d), z + (0.5 / 2d)), null);
                        for (EntityFishHook entity : entities) {
                            if(entity.angler == Minecraft.getMinecraft().thePlayer) {
                                slugFish = (Boolean)getConfigByFunctionName.get("AutoFish","slug");
                                if(slugFish) {
                                    if(hookTick < 0) {
                                        new AutoFish();
                                        AutoFishStatus = false;
                                        if ((Boolean) getConfigByFunctionName.get("AutoFish","message")) {
                                            utils.print("钓鱼检测状态:关闭");
                                        }
                                        hookTick = -1;
                                    }
                                } else {
                                    new AutoFish();
                                    AutoFishStatus = false;
                                    if ((Boolean) getConfigByFunctionName.get("AutoFish","message")) {
                                        utils.print("钓鱼检测状态:关闭");
                                    }
                                    hookTick = -1;
                                }
                            }
                        }
                    } else {
                        AutoFishStatus = true;
                        hookTick = 0;
                        if((Boolean) getConfigByFunctionName.get("AutoFish","message")) {
                            utils.print("钓鱼检测状态:开启");
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void PLayerInteract(PlayerInteractEvent event) {
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            try{
                if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    if (AutoFishStatus) {
                        AutoFishStatus = false;
                        if((Boolean) getConfigByFunctionName.get("AutoFish","message")) {
                            utils.print("钓鱼检测状态:关闭");
                        }
                    }
                }
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event){
        if(FunctionManager.getStatus("AutoFish")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                if ((Boolean)getConfigByFunctionName.get("AutoFish","timer")) {
                    double second = ((double) (hookTick)) / 40;
                    String NoticeString = "抛竿秒数: " + second;
                    mc.fontRendererObj.drawString(NoticeString, (new ScaledResolution(mc).getScaledWidth() / 2) - (mc.fontRendererObj.getStringWidth(NoticeString) / 2), (new ScaledResolution(mc).getScaledHeight() / 2) + 40, Antimony.Color);
                }
            }
        }
    }
}
