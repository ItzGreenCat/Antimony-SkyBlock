package com.greencat.common.ui;

import com.greencat.Antimony;
import com.greencat.common.storage.MessageStorage;
import com.greencat.type.Notice;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class NoticeManager {
    //ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    public static final ResourceLocation resourceLocation = new ResourceLocation(Antimony.MODID, "notice.png");

    public void Notice() {
        List<Notice> LocalNoticeList = MessageStorage.StorageList;
        List<Notice> NewList = new ArrayList<Notice>();
        try {
            for (Notice notice : LocalNoticeList) {
                if (notice.getY() != FunctionNotice.Location) {
                    notice.setY(notice.getY() + 1);
                    //System.out.println(notice.getX());
                }
                if (notice.getTime() != 200) {
                    Antimony.shouldRenderBossBar = false;
                    notice.timeAdd();
                    NewList.add(notice);
                } else {
                    Antimony.shouldRenderBossBar = true;
                    notice.remove();
                }
                notice.runDraw();
            }
            MessageStorage.setAllList(NewList);
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
}
