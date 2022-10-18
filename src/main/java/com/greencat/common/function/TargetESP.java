package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class TargetESP {
    public TargetESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static Double[] currentHeight = {0.0D};
    public static Boolean[] RenderStatus = {true};
    public static EntityLivingBase targetEntity;
    @SubscribeEvent
    public void AttackEntity(AttackEntityEvent event){
        if(FunctionManager.getStatus("TargetESP")){
            if(Minecraft.getMinecraft().theWorld != null){
                try{
                    if(event.entityPlayer == Minecraft.getMinecraft().thePlayer && event.target instanceof EntityLivingBase){
                        targetEntity = (EntityLivingBase) event.target;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    @SubscribeEvent
    public void Render(RenderWorldLastEvent event) {
        if(FunctionManager.getStatus("TargetESP")) {
            if (targetEntity != null && !targetEntity.isDead) {
                Utils.RenderTargetESP(targetEntity, new Color(25, 255, 251),2.5F,Math.max((float) ((targetEntity.getEntityBoundingBox().maxY - targetEntity.getEntityBoundingBox().minY) / 3.5),0.75F), currentHeight, RenderStatus);
            }
        }
    }
}
