package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.sound.SoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ThunderLord {
    public ThunderLord(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event){
        if(FunctionManager.getStatus("ThunderLord")){
            if(Minecraft.getMinecraft().theWorld != null){
                if(event.target instanceof EntityLivingBase){
                    Minecraft.getMinecraft().theWorld.spawnEntityInWorld(new EntityLightningBolt(Minecraft.getMinecraft().theWorld,event.target.posX,event.target.posY,event.target.posZ));
                    SoundPlayer.play(new ResourceLocation("ambient.weather.thunder"));
                    SoundPlayer.play(new ResourceLocation("random.explode"));
                }
            }
        }
    }
}
