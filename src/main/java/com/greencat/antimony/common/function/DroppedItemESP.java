package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class DroppedItemESP {
    public DroppedItemESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private static int colorR = 0;
    private static int colorG = 0;
    private static int colorB = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("DroppedItemESP")){
            colorR = (Integer) getConfigByFunctionName.get("DroppedItemESP","colorR");
            colorG = (Integer) getConfigByFunctionName.get("DroppedItemESP","colorG");
            colorB = (Integer) getConfigByFunctionName.get("DroppedItemESP","colorB");
        }
    }
    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {
        if(FunctionManager.getStatus("DroppedItemESP")){
            Double x = Minecraft.getMinecraft().thePlayer.posX;
            Double y = Minecraft.getMinecraft().thePlayer.posY;
            Double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityItem> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - (500 / 2d), y - (256 / 2d), z - (500 / 2d), x + (500 / 2d), y + (256 / 2d), z + (500 / 2d)), null);
            for(Entity entity : entityList){
                Utils.OutlinedBoxWithESP(new AxisAlignedBB(entity.posX - 0.25D, entity.posY - 0.25D, entity.posZ - 0.25D, entity.posX + 0.25D, entity.posY + 0.25D, entity.posZ + 0.25D),new Color(colorR,colorG,colorB),false,3.5F);
            }
        }
    }

}
