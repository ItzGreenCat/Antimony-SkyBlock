package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.utils.Chroma;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class ChestFinder {
    public ChestFinder() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(FunctionManager.getStatus("ChestFinder")){
            List<TileEntity> tileEntities = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
            for(TileEntity entity : tileEntities){
                if(entity instanceof TileEntityChest){
                    if((Integer) getConfigByFunctionName.get("ChestFinder","mode") == 0){
                        Utils.OutlinedBoxWithESP(entity.getPos(), Chroma.color,false,2);
                    }
                    if((Integer) getConfigByFunctionName.get("ChestFinder","mode") == 1){
                        Utils.BoxWithESP(entity.getPos(), Chroma.color,true);
                    }
                    if((Integer) getConfigByFunctionName.get("ChestFinder","mode") == 2){
                        Utils.OutlinedBoxWithESP(entity.getPos(), Chroma.color,false,2);
                        Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPosition(),entity.getPos(),Chroma.color,2);
                    }
                }
            }
        }
    }
}
