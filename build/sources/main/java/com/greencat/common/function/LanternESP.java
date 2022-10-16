package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class LanternESP {
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
    public LanternESP() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("LanternESP")){
            if(Minecraft.getMinecraft().theWorld != null) {
                this.player = Minecraft.getMinecraft().thePlayer;
                this.StartY = 64;
                this.StartX = (int) (this.player.posX - 64.0D);
                this.StartZ = (int) (this.player.posZ - 64.0D);
                this.EndX = (int) (this.player.posX + 64.0D);
                this.EndY = 130;
                this.EndZ = (int) (this.player.posZ + 64.0D);
            }
        }
    }
    @SubscribeEvent
    public void RenderESP(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("LanternESP")) {
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
                if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(NowX, NowY, NowZ)).getBlock() == Blocks.sea_lantern) {
                    Utils.OutlinedBoxWithESP(new BlockPos(NowX, NowY, NowZ), new Color(0,94,255),false,10);
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
