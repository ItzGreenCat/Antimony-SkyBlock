package com.GreenCat.common.function;

import com.GreenCat.Antimony;
import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Random;

public class AutoFish {
    public void AutoFishEventRegiser(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.func_71410_x();
    Utils utils = new Utils();
    Robot robot = new Robot();
    static int Tick = 40;
    Random randomYaw = new Random();
    Random randomPitch = new Random();
    int RandomNumber1 = randomYaw.nextInt(20);
    int RandomNumber2 = randomPitch.nextInt(20);


    public AutoFish() throws AWTException {
        try{
            if (FunctionManager.getStatus("AutoFish")) {
                if (mc.field_71439_g.func_70694_bm().func_77973_b() == Items.field_151112_aM) {
                    this.Tick = 0;
                    //utils.print("Fish now Up");
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if (mc.field_71441_e != null) {
            if (Tick < 40) {
                if (Tick == 5) {
                    mc.field_71439_g.field_70177_z = mc.field_71439_g.field_70177_z + RandomNumber1;
                    mc.field_71439_g.field_70125_A = mc.field_71439_g.field_70125_A + RandomNumber2;
                } else if (Tick == 10) {
                    Minecraft.func_71410_x().field_71442_b.func_78769_a(Minecraft.func_71410_x().field_71439_g, Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71439_g.func_70694_bm());
                } else if (Tick == 29) {
                    mc.field_71439_g.field_70125_A = mc.field_71439_g.field_70125_A - RandomNumber2;
                } else if(Tick == 37){
                    if(Antimony.AutoFishYawState) {
                        mc.field_71439_g.field_70177_z = (float) (mc.field_71439_g.field_70177_z - RandomNumber1 + 0.3);
                        Antimony.AutoFishYawState = false;
                    } else {
                        mc.field_71439_g.field_70177_z = (float) (mc.field_71439_g.field_70177_z - RandomNumber1 - 0.3);
                        Antimony.AutoFishYawState = true;
                    }
                } else if (Tick == 39) {
                    Minecraft.func_71410_x().field_71442_b.func_78769_a(Minecraft.func_71410_x().field_71439_g, Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71439_g.func_70694_bm());
                }
                Tick = Tick + 1;
            } else {
                Tick = 40;
            }
        }
    }
}
