package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class CrystalGetter {
    public static long lastTrigger = 0L;
    double range = 3.0D;
    public CrystalGetter() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && FunctionManager.getStatus("CrystalGetter")){
            if (System.currentTimeMillis() - lastTrigger > 250) {
                lastTrigger = System.currentTimeMillis();
                EntityEnderCrystal crystal = getTarget();
                if(crystal != null){
                    C02PacketUseEntity c02 = new C02PacketUseEntity(crystal, C02PacketUseEntity.Action.INTERACT);
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c02);
                }
            }
        }
    }
    private EntityEnderCrystal getTarget(){
        if ((!(Minecraft.getMinecraft().currentScreen instanceof GuiContainer) && Minecraft.getMinecraft().theWorld != null)) {
            double x = Minecraft.getMinecraft().thePlayer.posX;
            double y = Minecraft.getMinecraft().thePlayer.posY;
            double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityEnderCrystal> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(x - (range*2 / 2d), y - (range*2 / 2d), z - (range*2 / 2d), x + (range*2 / 2d), y + (range*2 / 2d), z + (range*2 / 2d)), null);
            for(EntityEnderCrystal entity : entities){
                return entity;
            }
        }
        return null;
    }
}
