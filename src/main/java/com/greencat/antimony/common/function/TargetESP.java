package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TargetESP {
    public TargetESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
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
                Utils.RenderTargetESP(targetEntity, Antimony.Color.AntimonyCyan);
            }
        }
    }
}
