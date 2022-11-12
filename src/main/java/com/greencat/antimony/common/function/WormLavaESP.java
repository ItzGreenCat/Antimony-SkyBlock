package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WormLavaESP {
    Utils utils = new Utils();
    int StartY;
    int StartX;
    int StartZ;
    EntityPlayer player;
    int EndX;
    int EndY;
    int EndZ;
    int NowX;
    int NowY;
    int NowZ;
    int Tick = 0;
    int RefreshTick = 200;
    List<BlockPos> LavaPosition;
    public WormLavaESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("WormLavaESP")){
            if(Minecraft.getMinecraft().theWorld != null) {
                if (Tick == RefreshTick) {
                    LavaPosition = new ArrayList<BlockPos>();
                    this.player = Minecraft.getMinecraft().thePlayer;
                    this.StartY = 64;
                    this.StartX = (int) (this.player.posX - 128.0D);
                    this.StartZ = (int) (this.player.posZ - 128.0D);
                    this.EndX = (int) (this.player.posX + 128.0D);
                    this.EndY = 130;
                    this.EndZ = (int) (this.player.posZ + 128.0D);

                    this.NowX = this.StartX;
                    this.NowY = this.StartY;
                    this.NowZ = this.StartZ;
                    while (NowY != EndY) {
                        if (this.NowX == this.EndX) {
                            if (this.NowZ == this.EndZ) {

                                this.NowZ = this.StartZ;
                                this.NowX = this.StartX;
                                NowY = NowY + 1;
                            } else {
                                this.NowX = this.StartX;
                                NowZ = NowZ + 1;
                            }
                        } else {
                            NowX = NowX + 1;
                        }
                        if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(NowX, NowY, NowZ)).getBlock() == Blocks.lava) {
                            LavaPosition.add(new BlockPos(NowX, NowY, NowZ));
                        }
                    }
                    this.NowX = this.StartX;
                    this.NowY = this.StartY;
                    this.NowZ = this.StartZ;
                    Tick = 0;
                    utils.print("WormLavaESP刷新完成，找到" + LavaPosition.size() + "个岩浆点");
                } else {
                    Tick = Tick + 1;
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderLavaESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("WormLavaESP")) {
            if(LavaPosition != null) {
                for (BlockPos position : LavaPosition) {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        Utils.BoxWithESP(position, new Color(0, 255, 255), false);
                    }
                }
            }
        }
    }
}
