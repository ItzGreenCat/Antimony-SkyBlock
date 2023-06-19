package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.cache.CachePoolProxy;
import com.greencat.antimony.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class StarredMobESP {
    public final CachePoolProxy<String,Boolean> cache = new CachePoolProxy<>(3,10,5);
    public StarredMobESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("StarredMobESP")) {
            if(event.entity instanceof EntityArmorStand && event.entity.hasCustomName()) {
                EntityArmorStand entity = (EntityArmorStand) event.entity;
                if (entity.hasCustomName()) {
                    if (cache.get(entity.getCustomNameTag(),() -> {
                        String nameString = StringUtils.stripControlCodes(entity.getCustomNameTag());
                        return nameString.contains("✯") && entity.getCustomNameTag().contains("❤");
                    })) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.posX - 0.5D, event.entity.posY - 2.0D, event.entity.posZ - 0.5D, event.entity.posX + 0.5D, event.entity.posY, event.entity.posZ + 0.5D), new Color(182, 255, 0), false,2.2F);
                    }
                }
            }
        }
    }
}
