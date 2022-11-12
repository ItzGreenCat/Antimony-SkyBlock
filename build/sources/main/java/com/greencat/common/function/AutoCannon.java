package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class AutoCannon {
    Utils utils = new Utils();
    boolean isOnCannon = false;
    static List<EntityWither> entities;
    public AutoCannon() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoCannon")) {
                if (isOnCannon) {
                    double x = Minecraft.getMinecraft().thePlayer.posX;
                    double y = Minecraft.getMinecraft().thePlayer.posY;
                    double z = Minecraft.getMinecraft().thePlayer.posZ;
                    entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWither.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                    if (!entities.isEmpty()) {
                        double[] PlayerLocationArray = {x, y, z};
                        double[] WitherLocationArray = {entities.get(0).posX, entities.get(0).posY - 10, entities.get(0).posZ};
                        Minecraft.getMinecraft().thePlayer.rotationYaw = (float) utils.FlatAngle(x, z, entities.get(0).posX, entities.get(0).posZ);
                        Minecraft.getMinecraft().thePlayer.rotationPitch = (float) utils.ErectAngle(PlayerLocationArray, WitherLocationArray);
                        if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null) {
                            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderEntity(RenderLivingEvent.Specials.Pre<EntityLivingBase> event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoCannon")) {
                if (isOnCannon) {
                    if (event.entity instanceof EntityWither) {
                        Utils.OutlinedBoxWithESP(event.entity.getEntityBoundingBox(), new Color(123, 254, 241), false, 3.5F);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event){
        if (FunctionManager.getStatus("AutoCannon")) {
            if (event.type == 2) {
                isOnCannon = event.message.getUnformattedText().contains("SHIFT") && event.message.getUnformattedText().contains("DISMOUNT");
            }
        }
    }

}
