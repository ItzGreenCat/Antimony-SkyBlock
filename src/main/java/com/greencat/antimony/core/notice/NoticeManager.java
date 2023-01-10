package com.greencat.antimony.core.notice;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class NoticeManager {
    public NoticeManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private static  List<Notice> notices = new ArrayList<>();
    public static void add(Notice notice){
        for(Notice currentNotice : notices){
            currentNotice.next(notice);
        }
        notices.add(notice);
        notice.init();
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event){
        List<Notice> removeList = new ArrayList<>();
        if(event.type == RenderGameOverlayEvent.ElementType.HELMET){
            try {
                for (Notice currentNotice : notices) {
                    if (System.currentTimeMillis() - currentNotice.startTime > 4500) {
                        currentNotice.remove();
                    }
                    if (!currentNotice.isAlive()) {
                        removeList.add(currentNotice);
                    }
                    currentNotice.render();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            for(Notice notice : removeList){
                notices.remove(notice);
            }
        }
    }
}
