package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class TerminalESP {
    public TerminalESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    String InactiveTerminal = "Inactive Terminal";
    String InactiveLevel = "Not Activate";
    String ActiveTerminal = "Terminal Active";

    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("TerminalESP")) {
            Entity entity = event.entity;
            if(entity instanceof EntityArmorStand) {
                if (entity.hasCustomName()) {
                    String nameString = StringUtils.stripControlCodes(entity.getCustomNameTag());
                    if (nameString.contains(InactiveLevel)) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(entity.posX - 0.5D, entity.posY - 0.5D, entity.posZ - 0.5D, entity.posX + 0.5D, entity.posY + 0.5D, entity.posZ + 0.5D), new Color(255, 64, 0), false, 10);
                    }
                    if (nameString.contains(InactiveTerminal)) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(entity.posX - 0.5D, entity.posY + 1.5D, entity.posZ - 0.5D, entity.posX + 0.5D, entity.posY + 2.5D, entity.posZ + 0.5D), new Color(255, 64, 0), false, 10);
                    }
                    if (nameString.contains(ActiveTerminal)) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(entity.posX - 0.5D, entity.posY + 1.5D, entity.posZ - 0.5D, entity.posX + 0.5D, entity.posY + 2.5D, entity.posZ + 0.5D), new Color(0, 255, 64), false, 10);
                    }
                }
            }
        }
    }
}
