package com.greencat.common.function.title;

import com.greencat.Antimony;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TitleManager {
    public static String tips = "";
    int tick = 599;
    final int MaxTick = 600;
    List<String> TipsList = new ArrayList<String>();

    public TitleManager(){
        MinecraftForge.EVENT_BUS.register(this);

        TipsList.add("kkxfj09:\"脸是什么，我不知道\"");
        TipsList.add("sheng23333：\"米大四 四大夫\"");
        TipsList.add("Wither Cloak是个不错的选择");
        TipsList.add("Antimony的中文是\"锑\"，符号是Sb");
        TipsList.add("你知道吗?Aspect of the Void只比Aspect of the End少消耗五点mana");
        TipsList.add("Aspect of the End可以打上Ether Warp");
        TipsList.add("如何刚出完Giant Sword接着马上出一个Ender Dragon Pet");
        TipsList.add("Spirit Mask在地牢外有效");
        TipsList.add("Slime Hat可以抵消真实击退");
        TipsList.add("Gyro Wand可以将真实伤害转换成普通伤害");
        TipsList.add("F7 P3的第一个Device被称作SS");
        TipsList.add("F7 P2的柱子判定时间为下压后3秒");
        TipsList.add("Crimson Armor曾经有无限攻速");
        TipsList.add("移除了Him");
        TipsList.add("Antimony的前身是GreenCatAddon");
        TipsList.add("Spirit Boots技能被禁用了");
        TipsList.add("为什么不尝试一下Portal 2这款游戏呢");
        TipsList.add("Slime Minion曾经是性价比最高的Minion");
        TipsList.add("这段文本存在于TitleManager.class");
        TipsList.add("System.out.println(\"Hello World\")");
        TipsList.add("3-5 business days");
        TipsList.add("Spirit non - 堂堂开混!");
        TipsList.add("You have been rat");
        TipsList.add("nothing here");
        TipsList.add("null");
        TipsList.add("Iron Punch (-50 Mana) * 3");
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(tick == MaxTick) {
            tick = 0;
            tips = TipsList.get(new Random().nextInt(TipsList.size()));
            Display.setTitle("Antimony " + Antimony.VERSION + " | " + tips + " (Minecraft 1.8.9)");
        } else {
            tick = tick + 1;
        }
    }
}
