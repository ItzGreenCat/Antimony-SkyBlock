package com.greencat.common.function.title;

import com.greencat.Antimony;
import com.greencat.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TitleManager {
    public static String tips = "";
    int tick = 599;
    final int MaxTick = 600;
    List<String> TipsList = new ArrayList<String>();

    public TitleManager(){
        MinecraftForge.EVENT_BUS.register(this);

        TipsList = Utils.getListInJsonFile("/TipsList.json");
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(tick == MaxTick) {
            tick = 0;
            tips = TipsList.get(new Random().nextInt(TipsList.size()));
            Display.setTitle("Antimony " + Antimony.VERSION + " | " + tips + " (Minecraft 1.8.9)");
        } else {
            tick = tick + 1;
        }
    }
}
