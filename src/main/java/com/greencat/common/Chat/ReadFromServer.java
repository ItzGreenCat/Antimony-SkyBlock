package com.greencat.common.Chat;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadFromServer {
    int tick = 0;
    static BufferedReader br;
    Thread thread;
    public ReadFromServer(){
        MinecraftForge.EVENT_BUS.register(this);
        refreshBufferedReader();
    }
    public static void refreshBufferedReader(){
        try {
            br = new BufferedReader(new InputStreamReader(AntimonyChannel.socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event){
        if(CheckConnect.isConnected){
        if(tick >= 30) {
            if (thread == null || !thread.isAlive()) {
                try {
                thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String msg;
                            try {
                                if ((msg = br.readLine()) != null) {
                                    new Utils().printAntimonyChannel(msg);
                                }
                            } catch (IOException ignored) {

                            }
                        }
                    });
                    thread.start();
                    System.gc();

                } catch (Exception ignored) {
                }
            }
        }
            tick = 0;
        } else {
            tick++;
        }
    }
}
