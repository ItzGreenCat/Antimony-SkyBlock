package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class ZealotMinion {
    Utils utils = new Utils();
    public ZealotMinion(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("ZealotMinion")) {
                Double x = Minecraft.getMinecraft().thePlayer.posX;
                Double y = Minecraft.getMinecraft().thePlayer.posY;
                Double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityEnderman> entity = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityEnderman.class, new AxisAlignedBB(x - (60 / 2d), y - (100 / 2d), z - (60 / 2d), x + (60 / 2d), y + (100 / 2d), z + (60 / 2d)), null);
                if (!entity.isEmpty()) {
                    double[] PlayerLocationArray = {x, y, z};
                    double[] EndermanLocationArray = {entity.get(0).posX, entity.get(0).posY, entity.get(0).posZ};
                    if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                        Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, (int) Minecraft.getMinecraft().thePlayer.rotationYaw, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                        double Angle = utils.FlatAngle(x, z, entity.get(0).posX, entity.get(0).posZ);
                        //utils.print(String.valueOf(Angle));
                        if (utils.RangeInDefined(Minecraft.getMinecraft().thePlayer.rotationYaw, Angle - 3, Angle + 3)) {
                            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                        }
                        if (Minecraft.getMinecraft().thePlayer.rotationYaw > Angle) {
                            if (Minecraft.getMinecraft().thePlayer.rotationYaw - Angle < 10) {
                                Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, Minecraft.getMinecraft().thePlayer.rotationYaw - 2, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            } else if (Minecraft.getMinecraft().thePlayer.rotationYaw - Angle > 10) {
                                Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, Minecraft.getMinecraft().thePlayer.rotationYaw - 8, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            }
                        } else if (Minecraft.getMinecraft().thePlayer.rotationYaw < Angle) {
                            if (Angle - Minecraft.getMinecraft().thePlayer.rotationYaw < 10) {
                                Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, Minecraft.getMinecraft().thePlayer.rotationYaw + 2, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            } else if (Angle - Minecraft.getMinecraft().thePlayer.rotationYaw > 10) {
                                Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, Minecraft.getMinecraft().thePlayer.rotationYaw + 8, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            }
                        }


                    }
                }
            }
        }
    }
}
