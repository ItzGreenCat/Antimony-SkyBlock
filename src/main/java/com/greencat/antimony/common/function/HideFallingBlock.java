package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HideFallingBlock {
    public HideFallingBlock() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void getFallingBlock(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("HideFallingBlock")) {
            Double x = Minecraft.getMinecraft().thePlayer.posX;
            Double y = Minecraft.getMinecraft().thePlayer.posY;
            Double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityFallingBlock> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityFallingBlock.class, new AxisAlignedBB(x - (500 / 2d), y - (256 / 2d), z - (500 / 2d), x + (500 / 2d), y + (256 / 2d), z + (500 / 2d)), null);
            for(Entity entity : entityList){
                if(entity instanceof EntityFallingBlock){
                    Minecraft.getMinecraft().theWorld.removeEntity(entity);
                }
            }
        }
    }
}
