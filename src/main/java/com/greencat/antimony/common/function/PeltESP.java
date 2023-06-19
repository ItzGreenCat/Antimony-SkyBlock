package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class PeltESP {
    public PeltESP(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event){
        if (FunctionManager.getStatus("PeltESP")) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    double x = Minecraft.getMinecraft().thePlayer.posX;
                    double y = Minecraft.getMinecraft().thePlayer.posY;
                    double z = Minecraft.getMinecraft().thePlayer.posZ;
                    List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (2000 / 2d), y - (256 / 2d), z - (2000 / 2d), x + (2000 / 2d), y + (256 / 2d), z + (2000 / 2d)), null);
                    for(Entity entity : entityList) {
                        if (entity instanceof EntityArmorStand) {
                            if (entity.hasCustomName()) {
                                if (isValid(entity.getCustomNameTag())) {
                                    Utils.OutlinedBoxWithESP(new AxisAlignedBB(entity.posX - 0.5D, entity.posY - 1.0D, entity.posZ - 0.5D, entity.posX + 0.5D, entity.posY, entity.posZ + 0.5D), Chroma.color, false, 3F);
                                    Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPosition(),entity.getPosition(),Chroma.color,2);
                                }
                            }
                        }
                    }
                }
        }
    }
    public boolean isValid(String name){
        String lowerName = name.toLowerCase();
        return (lowerName.contains("trackable") || lowerName.contains("untrackable") || lowerName.contains("undetected") || lowerName.contains("endangered") || lowerName.contains("elusive"));
    }
}
