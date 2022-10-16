package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class SilverfishESP {
    Utils utils = new Utils();
    public SilverfishESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderLavaESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("SilverfishESP")) {
            Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
            Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
            Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
            List<EntitySilverfish> entityList = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntitySilverfish.class, new AxisAlignedBB(x - (500 / 2d), y - (256 / 2d), z - (500 / 2d), x + (500 / 2d), y + (256 / 2d), z + (500 / 2d)), null);
            for(Entity entity : entityList){
                Utils.OutlinedBoxWithESP(entity.func_174813_aQ(),new Color(76,255,0),false);
            }
        }
    }
}
