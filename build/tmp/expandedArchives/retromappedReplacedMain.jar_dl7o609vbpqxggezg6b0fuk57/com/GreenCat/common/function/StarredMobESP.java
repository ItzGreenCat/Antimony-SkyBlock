package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class StarredMobESP {
    Utils utils = new Utils();
    public StarredMobESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderLavaESP(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("StarredMobESP")) {
            if(event.entity instanceof EntityArmorStand && event.entity.func_145818_k_()) {
                EntityArmorStand entity = (EntityArmorStand) event.entity;
                if (entity.func_145818_k_()) {
                    String nameString = StringUtils.func_76338_a(entity.func_95999_t());
                    if (nameString.contains("✯") && entity.func_95999_t().contains("❤")) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.field_70165_t - 0.5D, event.entity.field_70163_u - 2.0D, event.entity.field_70161_v - 0.5D, event.entity.field_70165_t + 0.5D, event.entity.field_70163_u, event.entity.field_70161_v + 0.5D), new Color(182, 255, 0), false,2.2F);
                    }
                }
            }
        }
    }
}
