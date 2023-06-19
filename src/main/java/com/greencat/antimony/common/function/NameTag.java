package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.greencat.antimony.core.PlayerNameFilter.isValid;

public class NameTag {
    public NameTag(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityPlayer> event){
        if(FunctionManager.getStatus("NameTag")){
            if (Minecraft.getMinecraft().theWorld != null) {
                if (event.entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) event.entity;
                    if (player != Minecraft.getMinecraft().thePlayer) {
                        if(isValid((EntityPlayer) event.entity,false)) {
                            double scale = (Double) ConfigInterface.get("NameTag","scale");
                            GlStateManager.pushMatrix();
                            Utils.renderNameTag(player,player.getPositionVector().addVector(-0.5D,1.8 + (scale * 0.5),-0.5D),((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks, (float) scale);
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
    }
}
