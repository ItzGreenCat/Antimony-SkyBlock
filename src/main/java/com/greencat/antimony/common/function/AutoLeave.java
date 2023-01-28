package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.HUDManager;
import com.greencat.antimony.utils.Utils;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.SoundSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import static com.greencat.antimony.core.PlayerNameFilter.isValid;

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
                List<EntityPlayer> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - (bound / 2d), y - (bound / 2d), z - (bound / 2d), x + (bound / 2d), y + (bound / 2d), z + (bound / 2d)), null);
                validList.clear();
                for (EntityPlayer entityplayer : entityList) {
                    if (isValid(entityplayer,true)) {
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
                    ISound sound = new PositionedSound(new ResourceLocation("random.orb")) {{
                        volume = 1;
                        pitch = 0.943f;
                        repeat = false;
                        repeatDelay = 0;
                        attenuationType = ISound.AttenuationType.NONE;
                    }};
                    countTick = 0;
                    if(!(Boolean) getConfigByFunctionName.get("AutoLeave", "soundOnly")) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage((String) getConfigByFunctionName.get("AutoLeave", "command"));
                    }
                    float oldLevel = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.PLAYERS);
                    Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1);
                    Minecraft.getMinecraft().getSoundHandler().playSound(sound);
                    Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel);
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
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (FunctionManager.getStatus("AutoLeave")) {
            Utils.print("检测到世界服务器改变,自动关闭AutoLeave");
            FunctionManager.setStatus("AutoLeave", false);
        }
    }
}
