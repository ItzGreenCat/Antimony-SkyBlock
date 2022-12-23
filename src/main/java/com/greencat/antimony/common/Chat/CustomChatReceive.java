package com.greencat.antimony.common.Chat;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CustomChatReceive {
    private static boolean active = true;
    static int tick = 0;
    public CustomChatReceive() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END){
           if(tick + 1 > 20){
               tick = 0;
               active = true;
           } else {
               tick = tick + 1;
           }
        }
    }
    public static void receive(){
        Thread thread = new Thread(() -> {
            while(true) {
                if(active) {
                    String context = null;
                    try {
                        DataInputStream out = new DataInputStream(AntimonyChannel.socket.getInputStream());
                        context = out.readUTF();
                    } catch (Exception e) {

                    }
                    try {
                        if (context != null) {
                            String[] packetContext = context.split("\\|");
                            List<String> message = new ArrayList<String>();
                            boolean culled = false;
                            for (String string : packetContext) {
                                if (!culled) {
                                    culled = true;
                                } else {
                                    message.add(string);
                                }
                            }
                            CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.CustomChannelReceivedEvent(Integer.parseInt(packetContext[0]), message));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        );
        try {
            thread.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
