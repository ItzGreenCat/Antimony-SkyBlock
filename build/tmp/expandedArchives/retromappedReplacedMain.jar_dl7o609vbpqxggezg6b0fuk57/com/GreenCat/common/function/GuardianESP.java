package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class GuardianESP {
    Utils utils = new Utils();
    public GuardianESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderLavaESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("GuardianESP")) {
            Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
            Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
            Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
            List<EntityGuardian> entityList = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityGuardian.class, new AxisAlignedBB(x - (500 / 2d), y - (256 / 2d), z - (500 / 2d), x + (500 / 2d), y + (256 / 2d), z + (500 / 2d)), null);
            for(Entity entity : entityList){
                Utils.OutlinedBoxWithESP(entity.func_174813_aQ(),new Color(0,38,255),false);
            }
        }
    }
}
