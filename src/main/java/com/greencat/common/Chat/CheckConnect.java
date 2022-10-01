package com.greencat.common.Chat;

import com.greencat.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class CheckConnect {
    static int tick = 0;
    public CheckConnect() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(tick >= 400){
            try{
                AntimonyChannel.socket.sendUrgentData(0xFF);
            }catch(Exception ex){
                try {
                    AntimonyChannel.socket.close();
                    new AntimonyChannel();
                    new Utils().print("Antimony Channel与服务器断开连接,正在重连");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            tick = 0;
        } else {
            tick = tick + 1;
        }
    }
}
