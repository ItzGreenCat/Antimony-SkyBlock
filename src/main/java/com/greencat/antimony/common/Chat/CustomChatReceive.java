package com.greencat.antimony.common.Chat;

import com.greencat.Antimony;
import com.greencat.antimony.core.event.CustomEventHandler;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CustomChatReceive {
    public static void receive(){
        Thread thread = new Thread(() -> {
            String context = null;
            try {
                Loop.receiveStatus = true;
                DataInputStream out = new DataInputStream(AntimonyChannel.socket.getInputStream());
                context = out.readUTF();
            } catch (Exception e) {
                Antimony.getjLogger().log(Level.WARNING,"Cannot read message from antimony channel");
            }
            try {
                if(context != null){
                    String[] packetContext = context.split("\\|");
                    List<String> message = new ArrayList<String>();
                    boolean culled = false;
                    for(String string : packetContext){
                        if(!culled){
                            culled = true;
                        } else {
                            message.add(string);
                        }
                    }
                    CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.CustomChannelReceivedEvent(Integer.parseInt(packetContext[0]),message));
                }
            } catch(Exception e){
                Antimony.getjLogger().log(Level.WARNING,"Cannot read packet context");
            }
            Loop.receiveStatus = false;
        }
        );
        try {
            thread.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
