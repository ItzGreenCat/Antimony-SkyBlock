package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Chroma;
import com.GreenCat.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public WormLavaESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("WormLavaESP")){
            if(Minecraft.func_71410_x().field_71441_e != null) {
                this.player = Minecraft.func_71410_x().field_71439_g;
                this.StartY = 64;
                this.StartX = (int) (this.player.field_70165_t - 64.0D);
                this.StartZ = (int) (this.player.field_70161_v - 64.0D);
                this.EndX = (int) (this.player.field_70165_t + 64.0D);
                this.EndY = 130;
                this.EndZ = (int) (this.player.field_70161_v + 64.0D);
            }
        }
    }
    @SubscribeEvent
    public void RenderLavaESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("WormLavaESP")) {
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
                if (Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(NowX, NowY, NowZ)).func_177230_c() == Blocks.field_150353_l) {
                    Utils.BoxWithESP(new BlockPos(NowX, NowY, NowZ), new Color(0,255,255),false);
                    //Utils.BoxWithESP(new BlockPos(NowX, NowY, NowZ), new Color(Chroma.getR(),Chroma.getG(),Chroma.getB()),false);
                    //utils.print(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(NowX,NowY,NowZ)).getBlock().toString());
                    //utils.print("NowX" + NowX + "NowY" + NowY + "NowZ" + NowZ);
                    //utils.print(Arrays.toString(lavaBlocks.toArray()));
                }
            }
            this.NowX = this.StartX;
            this.NowY = this.StartY;
            this.NowZ = this.StartZ;
        }
    }
}
