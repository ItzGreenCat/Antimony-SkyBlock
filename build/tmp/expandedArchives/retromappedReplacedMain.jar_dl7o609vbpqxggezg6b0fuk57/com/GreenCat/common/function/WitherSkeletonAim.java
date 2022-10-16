package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class WitherSkeletonAim {
    Utils utils = new Utils();
    public WitherSkeletonAim() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("WitherSkeletonAim")){
            if(Minecraft.func_71410_x().field_71441_e != null) {
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntitySkeleton> entities = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntitySkeleton.class, new AxisAlignedBB(x - (40 / 2d), y - (20 / 2d), z - (40 / 2d), x + (40 / 2d), y + (20 / 2d), z + (40 / 2d)), null);
                if(!entities.isEmpty()) {
                    double[] PlayerLocationArray = {x, y, z};
                    double[] SkeletonLocationArray = {entities.get(0).field_70165_t, entities.get(0).field_70163_u, entities.get(0).field_70161_v};
                            Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, (float) utils.FlatAngle(x, z, entities.get(0).field_70165_t, entities.get(0).field_70161_v), (float) utils.ErectAngle(PlayerLocationArray, SkeletonLocationArray));
                }
            }
        }
    }

}
