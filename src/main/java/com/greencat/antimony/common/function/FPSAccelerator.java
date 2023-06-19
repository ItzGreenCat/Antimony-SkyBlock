package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FPSAccelerator implements ReflectionlessEventHandler {
    static int armorStandDistance = 0;
    static int tileEntityDistance = 0;
    public static boolean enable = false;
    public FPSAccelerator(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START){
            if(FunctionManager.getStatus("FPS Accelerator")){
                enable = true;
                armorStandDistance = (Integer) ConfigInterface.get("FPS Accelerator","armorStandDistance");
                tileEntityDistance = (Integer) ConfigInterface.get("FPS Accelerator","tileEntityDistance");
            } else {
                enable = false;
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void RenderLivingEntityPre(RenderLivingEvent.Pre<EntityArmorStand> event){
        if(enable){
            if(event.entity instanceof EntityArmorStand){
                if(event.entity.getPositionVector().distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) > armorStandDistance){
                    event.setCanceled(true);
                }
            }
        }
    }
    public void RenderTileEntity(CustomEventHandler.RenderTileEntityPreEvent event){
        if(enable) {
            if (new Vec3(event.entity.getPos()).distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) > tileEntityDistance) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.RenderTileEntityPreEvent){
            RenderTileEntity((CustomEventHandler.RenderTileEntityPreEvent) event);
        }
    }
}
