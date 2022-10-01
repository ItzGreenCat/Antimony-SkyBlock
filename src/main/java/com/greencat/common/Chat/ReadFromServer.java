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
    static int cooldown = 0;
    public ReadFromServer(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            BufferedReader br;
            String msg;
            try {
                br = new BufferedReader(new InputStreamReader(AntimonyChannel.socket.getInputStream(), StandardCharsets.UTF_8));
                while((msg = br.readLine()) != null) {
                    new Utils().printAntimonyChannel(msg);
                }
            } catch (Exception ignored) {
                try {
                    AntimonyChannel.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new AntimonyChannel();
                new Utils().print("Antimony Channel与服务器断开连接,正在重连");
            }
        }
    };
    Thread thread = new Thread(runnable);
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AntimonyChannel")) {
            if (cooldown >= 10) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    try {
                        thread.start();
                    } catch (Exception ignored) {
                    }
                }
                cooldown = 0;
            } else {
                cooldown = cooldown + 1;
            }
        }
    }
}
