package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
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
    Utils utils = new Utils();
    public DungeonKeyESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void RenderLavaESP(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FunctionManager.getStatus("DungeonKeyESP")) {
            if(event.entity instanceof EntityArmorStand && event.entity.func_145818_k_()) {
                EntityArmorStand entity = (EntityArmorStand) event.entity;
                if (entity.func_145818_k_()) {
                    String nameString = StringUtils.func_76338_a(entity.func_95999_t());
                    if (nameString.contains("Wither Key")) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.field_70165_t - 0.5D, event.entity.field_70163_u + 1, event.entity.field_70161_v - 0.5D, event.entity.field_70165_t + 0.5D, event.entity.field_70163_u + 2D, event.entity.field_70161_v + 0.5D), new Color(32, 32, 32), false,3.5F);
                    }
                    if (nameString.contains("Blood Key")) {
                        Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.field_70165_t - 0.5D, event.entity.field_70163_u + 1, event.entity.field_70161_v - 0.5D, event.entity.field_70165_t + 0.5D, event.entity.field_70163_u + 2D, event.entity.field_70161_v + 0.5D), new Color(255, 25, 50), false,3.5F);
                    }
                }
            }
        }
    }
}
