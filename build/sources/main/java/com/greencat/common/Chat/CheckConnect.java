package com.greencat.common.Chat;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.io.PrintStream;

public class CheckConnect {
    static int tick = 0;
    PrintStream ps;
    public CheckConnect() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AntimonyChannel")) {
            if (tick >= 400) {
                new Thread(() -> {
                    try {
                        ps = new PrintStream(AntimonyChannel.socket.getOutputStream(), false, "UTF-8");
                        ps.println("CHECK_CONNECT");
                        ps.flush();
                    } catch (Exception ex) {
                        try{
                            AntimonyChannel.socket.close();
                        } catch(Exception exc){
                            exc.printStackTrace();
                        }
                        new AntimonyChannel();
                        ReadFromServer.refreshBufferedReader();
                /*ReadFromServer.StopThread = true;
                new ReadFromServer();*/
                        new Utils().print("Antimony Channel与服务器断开连接,正在重连");
                    }
                    try {
                        AntimonyChannel.socket.sendUrgentData(0xFF);
                    } catch (Exception ex) {
                        try{
                            AntimonyChannel.socket.close();
                        } catch(Exception exc){
                            exc.printStackTrace();
                        }
                        new AntimonyChannel();
                        ReadFromServer.refreshBufferedReader();
                /*ReadFromServer.StopThread = true;
                new ReadFromServer();*/
                        new Utils().print("Antimony Channel与服务器断开连接,正在重连");
                    }
                }).start();
                tick = 0;
            } else {
                tick = tick + 1;
            }
        }
    }
}
