package com.GreenCat.common.ui;

import com.GreenCat.common.storage.MessageStorage;
import com.GreenCat.type.Notice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class FunctionNotice {
    public static int Location;
    public void ShowNotice(String message,boolean isEnable){
        Notice notice = new Notice();
        notice.SetNotice(message,isEnable);
        MessageStorage.AddNotice(notice);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        Location = 0;
        /*System.out.println(scaledResolution.getScaledWidth());
        System.out.println(scaledResolution.getScaledWidth() - (scaledResolution.getScaledWidth() / 6));*/
        //System.out.println(Location);

    }
}
