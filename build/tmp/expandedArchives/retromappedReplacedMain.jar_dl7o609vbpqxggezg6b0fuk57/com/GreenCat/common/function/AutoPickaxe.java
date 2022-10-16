package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AutoPickaxe {
    Utils utils = new Utils();
    public AutoPickaxe() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void MiningTrigger(PlayerInteractEvent event) throws NullPointerException{
        if(FunctionManager.getStatus("AutoPickaxe")) {
            if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70461_c = Utils.FindPickaxeInHotBar();
            }
        }
    }
}
