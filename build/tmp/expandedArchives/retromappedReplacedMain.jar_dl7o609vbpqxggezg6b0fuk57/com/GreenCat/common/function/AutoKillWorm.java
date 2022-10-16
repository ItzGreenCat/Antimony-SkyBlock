package com.GreenCat.common.function;

import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoKillWorm {
    int Tick = 0;
    public void EventRegister(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.func_71410_x();
    Utils utils = new Utils();
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        /*if(FunctionManager.getStatus("AutoKillWorm")) {
            if (Tick < 6000) {
                Tick = Tick + 1;
            } else {
                if (mc.theWorld != null) {
                    if (FunctionManager.getStatus("AutoFish")) {
                        utils.print("已经关闭AutoFish");
                        FunctionManager.setStatus("AutoFish", false);
                    }
                    mc.thePlayer.inventory.currentItem = GreenCatAddons.SwordIndex;
                    Double x = Minecraft.getMinecraft().thePlayer.posX;
                    Double y = Minecraft.getMinecraft().thePlayer.posY;
                    Double z = Minecraft.getMinecraft().thePlayer.posZ;
                    List<EntitySilverfish> entity = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntitySilverfish.class, new AxisAlignedBB(x - 2, y - 2, z - 2, x + 2, y + 0, z + 2), null);
                    if (!entity.isEmpty()) {
                            double[] PlayerLocationArray = {x, y, z};
                            double[] WormLocationArray = {entity.get(0).posX, entity.get(0).posY, entity.get(0).posZ};
                            Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, (int) Minecraft.getMinecraft().thePlayer.rotationYaw, (float) utils.ErectAngle(PlayerLocationArray, WormLocationArray));
                            double Angle = utils.FlatAngle(x, z, entity.get(0).posX, entity.get(0).posZ);
                            //utils.print(String.valueOf(Angle));
                            Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, (float) Angle, (float) utils.ErectAngle(PlayerLocationArray, WormLocationArray));

                            Minecraft.getMinecraft().playerController.attackEntity(mc.thePlayer, entity.get(0));                    } else {
                        Minecraft.getMinecraft().thePlayer.rotationYaw = GreenCatAddons.AutoFishYaw;
                        Minecraft.getMinecraft().thePlayer.rotationPitch = 0;
                        FunctionManager.setStatus("AutoFish", true);
                        mc.thePlayer.inventory.currentItem = GreenCatAddons.RodIndex;
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                        Tick = 0;
                    }
                }
            }
        }*/
    }

}
