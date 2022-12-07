package com.greencat.antimony.common.Chat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Loop {
    public static boolean receiveStatus = false;
    public Loop(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    int tick = 0;
    @SubscribeEvent
    public void onReceive(TickEvent.ClientTickEvent event){
        if(!receiveStatus){
            if(event.phase == TickEvent.Phase.END) {
                if(tick + 1 > 10) {
                    CustomChatReceive.receive();
                    tick = 0;
                } else {
                    tick = tick + 1;
                }
            }
        }
    }
}
