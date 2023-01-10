package com.greencat.antimony.core.ui;

import com.greencat.Antimony;
import com.greencat.antimony.core.notice.FunctionNoticeManager;
import com.greencat.antimony.utils.sound.SoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class FunctionNotice {
    public static void ShowNotice(String message,boolean isEnable){
        FunctionNoticeManager.add(message,isEnable);
    }
    public static void ShowNoticeSound(String message,boolean isEnable){
        FunctionNoticeManager.add(message,isEnable);
        if(isEnable){
            SoundPlayer.play(new ResourceLocation("antimony:function.enable"));
        } else {
            SoundPlayer.play(new ResourceLocation("antimony:function.disable"));
        }

    }
}
