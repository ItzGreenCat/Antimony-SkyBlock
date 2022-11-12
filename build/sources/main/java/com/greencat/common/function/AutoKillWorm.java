package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.utils.EasyReflection;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoKillWorm {
    public static long latest;
    static boolean TranslateOn;
    int Tick = 0;
    public AutoKillWorm(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if(Minecraft.getMinecraft().theWorld != null) {
                if (Tick < 12000) {
                    Tick = Tick + 1;
                } else {
                    if (mc.theWorld != null) {
                        if (FunctionManager.getStatus("AutoFish")) {
                            utils.print("已经关闭AutoFish");
                            FunctionManager.setStatus("AutoFish", false);
                        }
                        if (FunctionManager.getStatus("ItemTranslate")) {
                            FunctionManager.setStatus("ItemTranslate", false);
                            TranslateOn = true;
                        } else {
                            TranslateOn = false;
                        }

                        if (!ActiveStaff()) {
                            utils.print("无法找到Staff of the Volcano");
                        }
                        if (TranslateOn) {
                            FunctionManager.setStatus("ItemTranslate", true);
                        }
                        FunctionManager.setStatus("AutoFish", true);
                        if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null){
                            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                        }
                        Tick = 0;
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event){
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                double second = ((double) (12000 - Tick)) / 40;
                String NoticeString = "AutoKillWorm剩余秒数: " + second;
                mc.fontRendererObj.drawString(NoticeString, (new ScaledResolution(mc).getScaledWidth() / 2) - (mc.fontRendererObj.getStringWidth(NoticeString) / 2), (new ScaledResolution(mc).getScaledHeight() / 2) + 20, Antimony.Color);
            }
        }
    }

    public boolean ActiveStaff() {
        boolean foundStaff = false;
        if (System.currentTimeMillis() - latest >= 0) {
            latest = System.currentTimeMillis();
            for (int i = 0; i < 8; ++i) {
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains("of the volcano")) {
                    int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, stack);
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = currentSlot;
                    foundStaff = true;
                    break;
                }
            }
        }
        return foundStaff;
    }
    @SubscribeEvent
    public void WorldChangeTrigger(ClientChatReceivedEvent event) {
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("You are playing on profile")) {
                new Utils().print("检测到世界服务器改变,自动关闭AutoWormKill");
                FunctionManager.setStatus("AutoKillWorm",false);
            }
        }
    }

}
