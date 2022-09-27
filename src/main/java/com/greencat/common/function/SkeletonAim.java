package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class SkeletonAim {
    Utils utils = new Utils();
    public SkeletonAim() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("SkeletonAim")){
            if(Minecraft.getMinecraft().theWorld != null) {
                Double x = Minecraft.getMinecraft().thePlayer.posX;
                Double y = Minecraft.getMinecraft().thePlayer.posY;
                Double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntitySkeleton> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntitySkeleton.class, new AxisAlignedBB(x - (40 / 2d), y - (20 / 2d), z - (40 / 2d), x + (40 / 2d), y + (20 / 2d), z + (40 / 2d)), null);
                if(!entities.isEmpty()) {
                    double[] PlayerLocationArray = {x, y, z};
                    double[] SkeletonLocationArray = {entities.get(0).posX, entities.get(0).posY, entities.get(0).posZ};
                            Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, (float) utils.FlatAngle(x, z, entities.get(0).posX, entities.get(0).posZ), (float) utils.ErectAngle(PlayerLocationArray, SkeletonLocationArray));
                }
            }
        }
    }

}
