package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.core.HUDManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class AutoLeave {
    static List<EntityPlayer> validList = new ArrayList<EntityPlayer>();
    static int countTick = 0;
    public AutoLeave(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoLeave")) {
            if(Minecraft.getMinecraft().theWorld != null) {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                int bound = (Integer) getConfigByFunctionName.get("AutoLeave", "radius") * 2;
                List<EntityPlayer> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - (bound / 2d), y - (256 / 2d), z - (bound / 2d), x + (bound / 2d), y + (256 / 2d), z + (bound / 2d)), null);
                validList.clear();
                for (EntityPlayer entityplayer : entityList) {
                    if (isValid(entityplayer)) {
                        validList.add(entityplayer);
                    }
                }
                int playerLimit = (Integer) getConfigByFunctionName.get("AutoLeave", "limit");
                if (validList.size() > playerLimit) {
                    countTick = countTick + 1;
                } else {
                    countTick = 0;
                }
                int tickLimit = (Integer) getConfigByFunctionName.get("AutoLeave", "tickLimit");
                if (countTick > tickLimit) {
                    countTick = 0;
                    Minecraft.getMinecraft().thePlayer.sendChatMessage((String) getConfigByFunctionName.get("AutoLeave", "command"));
                }
            }
        }
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if(FunctionManager.getStatus("AutoLeave")){
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                if(Minecraft.getMinecraft().theWorld != null) {
                    HUDManager.Render("Nearly Player", validList.size(), 200, 220);
                    HUDManager.Render("Player Limit", getConfigByFunctionName.get("AutoLeave", "limit"), 200, 235);
                    HUDManager.Render("Command Timer", countTick, 200, 250);
                    HUDManager.Render("Max Command Tick", getConfigByFunctionName.get("AutoLeave", "tickLimit"), 200, 275);
                    HUDManager.Render("Command", getConfigByFunctionName.get("AutoLeave", "command"), 200, 290);
                }
            }
        }
    }
    public Boolean isValid(EntityPlayer player){
        return !Utils.isNPC(player) && !player.isInvisible() && player != Minecraft.getMinecraft().thePlayer;
    }
}
