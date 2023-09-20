package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.notice.Notice;
import com.greencat.antimony.core.notice.NoticeManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientCh`atReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Rat {
    Utils utils = new Utils();
    String Rat = "之前有人给我发了shady的github链接，偶然看到个这个,就拿过来了lol,顺便拿这个做了个彩蛋功能";
    long lastTrigger = 0;
    public Rat() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static String a = "                  ..----.._    _              ";
    public static String b = "                .' .--.    '-.(O)_            ";
    public static String c = "    '-.__.-'''=:|  ,  _)_ |__ . c\\'-..        ";
    public static String d = "                 ''------'---''---'-'         ";
    @SubscribeEvent
    public void showRat(WorldEvent.Load event) {
        if(FunctionManager.getStatus("Rat") && System.currentTimeMillis() - lastTrigger > 10000) {
            lastTrigger = System.currentTimeMillis();
            new Thread(() -> {
                    try {
                        Thread.sleep(7000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Minecraft.getMinecraft().theWorld != null) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "You have been Ratted"));
                        Utils.print(a)
                        Utils.print(b)
                        Utils.print(c)
                        Utils.print(d)
                    }
                    Notice notice = new Notice("A Cute Rat", true, "You have been Ratted");
                    Notice rat = new Notice("A Cute Rat", true, a, b, c, d);
                    NoticeManager.add(notice);
                    NoticeManager.add(rat);
            }).start();
        }
    }

}
