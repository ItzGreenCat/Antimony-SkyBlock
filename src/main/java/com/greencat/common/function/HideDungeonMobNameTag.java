package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideDungeonMobNameTag {
    public HideDungeonMobNameTag() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyEntityTag(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("HideDungeonMobNameTag")) {
            Entity entity = event.entity;
            if (entity instanceof EntityArmorStand) {
                if (entity.hasCustomName()) {
                    if(StringUtils.stripControlCodes(entity.getCustomNameTag()).toLowerCase().contains("zombie") ||
                            StringUtils.stripControlCodes(entity.getCustomNameTag()).toLowerCase().contains("skele") ||
                            StringUtils.stripControlCodes(entity.getCustomNameTag()).toLowerCase().contains("crypt") ||
                            StringUtils.stripControlCodes(entity.getCustomNameTag()).toLowerCase().contains("wither")) {
                        entity.setAlwaysRenderNameTag(false);
                    }
                }
            }
        }
    }
}
