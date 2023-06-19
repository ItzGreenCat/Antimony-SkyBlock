package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class GhostBlockCreator {
    public static long lastSearch = 0L;
    public GhostBlockCreator() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || !FunctionManager.getStatus("GhostBlockCreator")){
            return;
        }
        if(event.phase == TickEvent.Phase.START){
            if(System.currentTimeMillis() - lastSearch >= 250L){
                lastSearch = System.currentTimeMillis();
                BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(3,3,3),Minecraft.getMinecraft().thePlayer.getPosition().add(-3,-3,-3))
                        .forEach(it -> {
                            if((Boolean)get("GhostBlockCreator","iron_bar") && Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(it).getBlock() == Blocks.iron_bars){
                                Minecraft.getMinecraft().thePlayer.worldObj.setBlockToAir(it);
                            }
                            if((Boolean)get("GhostBlockCreator","carpet") && Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(it).getBlock() == Blocks.carpet){
                                Minecraft.getMinecraft().thePlayer.worldObj.setBlockToAir(it);
                            }
                        });
            }
        }
    }
}
