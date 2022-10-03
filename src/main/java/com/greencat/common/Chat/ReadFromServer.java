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
    /*public static boolean StopThread = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            BufferedReader br;
            String msg;
            try {
                br = new BufferedReader(new InputStreamReader(AntimonyChannel.socket.getInputStream(), StandardCharsets.UTF_8));
                while((msg = br.readLine()) != null) {
                    if(!StopThread) {
                        new Utils().printAntimonyChannel(msg);
                    } else {
                        StopThread = false;
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    };*/
    static BufferedReader br;
    public ReadFromServer(){
        /*Thread thread = new Thread(runnable);
        thread.start();*/
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
        if(tick >= 30) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg;
                        try {
                            if ((msg = br.readLine()) != null) {
                                new Utils().printAntimonyChannel(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (Exception ignored) {
            }
            tick = 0;
        } else {
            tick++;
        }
    }
}
