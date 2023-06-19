package com.greencat.antimony.common.Chat;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CheckConnect {
    static int tick = 0;
    public CheckConnect() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AntimonyChannel")) {
            if (tick >= 4000) {
                new Thread(() -> {
                    try {
                        AntimonyChannel.socket.sendUrgentData(0xFF);
                    } catch (Exception ex) {
                        try{
                            AntimonyChannel.socket.close();
                        } catch(Exception exc){
                            exc.printStackTrace();
                        }
                        AntimonyChannel.reconnect();
                        if((Boolean) ConfigInterface.get("AntimonyChannel","notice")) {
                            new Utils().print("Antimony Channel与服务器断开连接,正在重连");
                        }
                    }
                }).start();
                tick = 0;
            } else {
                tick = tick + 1;
            }
        }
    }
}
