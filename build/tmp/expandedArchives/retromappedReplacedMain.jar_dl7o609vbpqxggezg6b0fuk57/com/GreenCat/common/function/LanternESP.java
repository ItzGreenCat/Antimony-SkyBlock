package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
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
                if (Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(NowX, NowY, NowZ)).func_177230_c() == Blocks.field_180398_cJ) {
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
