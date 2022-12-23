package com.greencat.antimony.core;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.type.DanmakuMessage;
import com.greencat.antimony.develop.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class DanmakuCore {
    private static final List<DanmakuMessage> messageQueue = new ArrayList<DanmakuMessage>();
    private static final List<DanmakuMessage> displayingMessage = new ArrayList<DanmakuMessage>();
    int cooldown = 0;
    boolean move = false;

    public DanmakuCore() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void addMessageToQueue(DanmakuMessage message) {
        messageQueue.add(message);
        Console.addMessage("New Message input: " + message);
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent event) {
        if(FunctionManager.getStatus("DanmakuChat")) {
            if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
                ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                if (!messageQueue.isEmpty()) {
                    DanmakuMessage message = messageQueue.get(0);
                    int freeTrack = getFreeTrack();
                    if (freeTrack != 0) {
                        int yPos = getTrackPosition(freeTrack);
                        if (!(yPos > ((scaledResolution.getScaledHeight() / 6) * 5))) {
                            message.track = freeTrack;
                            message.x = scaledResolution.getScaledWidth();
                            message.refreshEndX();
                            message.y = yPos;
                            displayingMessage.add(message);
                            messageQueue.remove(0);
                        }
                    }
                }
                if (cooldown + 1 > 5) {
                    move = true;
                    cooldown = 0;
                } else {
                    cooldown = cooldown + 1;
                }
                if (!displayingMessage.isEmpty()) {
                    for (DanmakuMessage message : displayingMessage) {
                        if (move) {
                            message.x = message.x - 2;
                        }
                        message.draw();
                    }
                    move = false;
                    destroyMessage();
                }
                Console.addMessage("displayedMessage size: " + displayingMessage.size());
            }
        }
    }

    public void destroyMessage() {
        List<DanmakuMessage> destroyList = new ArrayList<DanmakuMessage>();
        for (DanmakuMessage message : displayingMessage) {
            if (message.endX < 0) {
                destroyList.add(message);
            }
        }
        for (DanmakuMessage message : destroyList) {
            displayingMessage.remove(message);
        }
    }

    public int getFreeTrack() {
        Console.addMessage("start to get track");
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (!displayingMessage.isEmpty()) {
            boolean finished = false;
            int returnValue = 0;
            int currentTrack = 1;
            while (!finished) {
                boolean currentStatus = false;
                for (DanmakuMessage message : displayingMessage) {
                    if (message.track == currentTrack) {
                        message.refreshEndX();
                        if ((message.endX + 5) > scaledResolution.getScaledWidth()) {
                            currentStatus = true;
                        }
                    }
                }
                if (currentStatus) {
                    currentTrack = currentTrack + 1;
                } else {
                    returnValue = currentTrack;
                    finished = true;
                }
                Console.addMessage("returned current track: " + currentTrack);
            }
            return returnValue;
        } else {
            Console.addMessage("returned track 1");
            return 1;
        }
    }

    public int getTrackPosition(int id) {
        Console.addMessage("trying to get track position by id: " + id);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int finalPosition = scaledResolution.getScaledHeight() / 6;
        int round = id;
        while (round != 0) {
            finalPosition = finalPosition + 13;
            round = round - 1;
        }
        return finalPosition;
    }
}
