package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.BlockScanner;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.timer.CustomTimer;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DragonEggESP {
    static boolean checked;
    List<BlockPos> Position;
    BlockScanner scanner = new BlockScanner();
    public DragonEggESP() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if (event.function.getName().equals("DragonEggESP")) {
            init();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if (event.function.getName().equals("DragonEggESP") && event.status) {
            init();
        }
    }
    public void init(){
        if(Minecraft.getMinecraft().theWorld != null) {
            checked = false;
            Utils.print("开始扫描,重开启功能可以再次扫描");
            scanner.start(
                    new BlockPos(Minecraft.getMinecraft().thePlayer.posX + 256, 0, Minecraft.getMinecraft().thePlayer.posZ + 256),
                    new BlockPos(Minecraft.getMinecraft().thePlayer.posX - 256, 256, Minecraft.getMinecraft().thePlayer.posZ - 256),
                    Blocks.dragon_egg, (Integer) getConfigByFunctionName.get("DragonEggESP", "step"), (Integer) getConfigByFunctionName.get("DragonEggESP", "thread")
            );
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("DragonEggESP")) {
            if (event.phase == TickEvent.Phase.END) {
                if (scanner.finished && !checked) {
                    Position = scanner.finalList;
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("DragonEggESP")) {
            if(Position != null) {
                for (BlockPos position : Position) {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        Utils.BoxWithESP(position, new Color(164, 21, 145), false);
                    }
                }
            }
        }
    }
}
