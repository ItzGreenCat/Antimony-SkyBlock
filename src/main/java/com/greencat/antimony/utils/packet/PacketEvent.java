package com.greencat.antimony.utils.packet;

import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketEvent {
    public PacketEvent() {
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void packetReceived(CustomEventHandler.PacketReceivedEvent event){
        CustomEventHandler.PacketEvent packetEvent = new CustomEventHandler.PacketEvent(event.packet);
        CustomEventHandler.EVENT_BUS.post(packetEvent);
        if(packetEvent.isCanceled()){
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void packetSend(CustomEventHandler.PacketSentEvent event){
        CustomEventHandler.PacketEvent packetEvent = new CustomEventHandler.PacketEvent(event.packet);
        CustomEventHandler.EVENT_BUS.post(packetEvent);
        if(packetEvent.isCanceled()){
            event.setCanceled(true);
        }
    }
}
