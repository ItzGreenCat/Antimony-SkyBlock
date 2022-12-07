package com.greencat.antimony.common.function;

import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IRC {
    public IRC() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onCustomChatReceived(CustomEventHandler.CustomChannelReceivedEvent event){
        if(event.id == 0){
            try {
                Utils.printAntimonyChannel(event.context.get(0), event.context.get(1));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
