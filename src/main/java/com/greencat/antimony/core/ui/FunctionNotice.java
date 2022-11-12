package com.greencat.antimony.core.ui;

import com.greencat.antimony.core.storage.MessageStorage;
import com.greencat.antimony.core.type.Notice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class FunctionNotice {
    public static int Location;
    public void ShowNotice(String message,boolean isEnable){
        Notice notice = new Notice();
        notice.SetNotice(message,isEnable);
        MessageStorage.AddNotice(notice);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Location = 0;

    }
}
