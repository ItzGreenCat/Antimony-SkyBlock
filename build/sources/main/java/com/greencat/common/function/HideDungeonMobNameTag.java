package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HideDungeonMobNameTag {
    public boolean isInTheCatacombs = false;

    public HideDungeonMobNameTag() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyEntityTag(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("HideDungeonMobNameTag")) {
            if (isInDungeon()) {
                Entity entity = event.entity;
                if (entity instanceof EntityArmorStand) {
                    if (entity.hasCustomName()) {
                        if (StringUtils.stripControlCodes(entity.getCustomNameTag()).toLowerCase().contains("zombie") ||
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
    private boolean isInDungeon() {
        isInTheCatacombs = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                isInTheCatacombs = true;
            }
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                isInTheCatacombs = true;
            }
        }
        return isInTheCatacombs;
    }
}
