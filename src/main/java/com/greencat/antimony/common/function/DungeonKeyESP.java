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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class DungeonKeyESP {
    public final CachePoolProxy<String,Boolean> witherKeyCache = new CachePoolProxy<>(3,5,2);
    public final CachePoolProxy<String,Boolean> bloodKeyCache = new CachePoolProxy<>(3,5,2);
    public DungeonKeyESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void RenderESP(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("DungeonKeyESP")) {
            if(event.entity instanceof EntityArmorStand && event.entity.hasCustomName()) {
                EntityArmorStand entity = (EntityArmorStand) event.entity;
                if (entity.hasCustomName()) {
                    if (witherKeyCache.get(entity.getCustomNameTag(),() -> {
                        String nameString = StringUtils.stripControlCodes(entity.getCustomNameTag());
                        return nameString.contains("Wither Key");
                    })) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.posX - 0.5D, event.entity.posY + 1, event.entity.posZ - 0.5D, event.entity.posX + 0.5D, event.entity.posY + 2D, event.entity.posZ + 0.5D), new Color(32, 32, 32), false,3.5F);
                    }
                    if (bloodKeyCache.get(entity.getCustomNameTag(),() -> {
                        String nameString = StringUtils.stripControlCodes(entity.getCustomNameTag());
                        return nameString.contains("Blood Key");
                    })) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.posX - 0.5D, event.entity.posY + 1, event.entity.posZ - 0.5D, event.entity.posX + 0.5D, event.entity.posY + 2D, event.entity.posZ + 0.5D), new Color(255, 25, 50), false,3.5F);
                    }
                }
            }
        }
    }
}
