package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class WitherESP {
    public WitherESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("WitherESP")) {
            double x = Minecraft.getMinecraft().thePlayer.posX;
            double y = Minecraft.getMinecraft().thePlayer.posY;
            double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityWither> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWither.class, new AxisAlignedBB(x - (500 / 2d), y - (256 / 2d), z - (500 / 2d), x + (500 / 2d), y + (256 / 2d), z + (500 / 2d)), null);
            for(Entity entity : entityList){
                Utils.OutlinedBoxWithESP(entity.getEntityBoundingBox(),new Color(0, 255, 178, 255),false);
            }
        }
    }
}
