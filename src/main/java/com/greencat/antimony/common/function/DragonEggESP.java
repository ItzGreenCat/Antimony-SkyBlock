package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
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
    Utils utils = new Utils();
    int StartY,StartX,StartZ;
    EntityPlayer player;
    int EndX,EndY,EndZ,NowX,NowY,NowZ;   //主播 是有逗号这一说的 - Pysio
    int Tick = 0;
    int RefreshTick = 200;
    List<BlockPos> Position;
    public DragonEggESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("DragonEggESP")){
            if(Minecraft.getMinecraft().theWorld != null) {
                if (Tick == RefreshTick) {
                    Position = new ArrayList<BlockPos>();
                    this.player = Minecraft.getMinecraft().thePlayer;
                    this.StartY = 0;
                    this.StartX = (int) (this.player.posX - 128.0D);
                    this.StartZ = (int) (this.player.posZ - 128.0D);
                    this.EndX = (int) (this.player.posX + 128.0D);
                    this.EndY = 256;
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
                        if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(NowX, NowY, NowZ)).getBlock() == Blocks.dragon_egg) {
                            Position.add(new BlockPos(NowX, NowY, NowZ));
                        }
                    }
                    this.NowX = this.StartX;
                    this.NowY = this.StartY;
                    this.NowZ = this.StartZ;
                    Tick = 0;
                    utils.print("刷新完成，找到" + Position.size() + "个龙蛋");
                } else {
                    Tick = Tick + 1;
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
