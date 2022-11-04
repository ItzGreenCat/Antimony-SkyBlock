package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.core.HUDManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoKillWorm {
    public static long latest;
    static boolean TranslateOn;
    static boolean aim;
    static boolean Ticking;
    Float[] rotations = new Float[2];
    int Tick = 0;
    public AutoKillWorm(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if(event.phase == TickEvent.Phase.START) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (Tick < ((Integer) getConfigByFunctionName.get("AutoKillWorm", "cooldown") * 40)) {
                        Tick = Tick + 1;
                    } else {
                        if (mc.theWorld != null) {
                            aim = (Boolean) getConfigByFunctionName.get("AutoKillWorm", "aim");
                            if (aim) {
                                rotations[0] = Minecraft.getMinecraft().thePlayer.rotationYaw;
                                rotations[1] = Minecraft.getMinecraft().thePlayer.rotationPitch;
                                Double x = Minecraft.getMinecraft().thePlayer.posX;
                                Double y = Minecraft.getMinecraft().thePlayer.posY;
                                Double z = Minecraft.getMinecraft().thePlayer.posZ;
                                List<EntitySilverfish> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntitySilverfish.class, new AxisAlignedBB(x - (20 / 2d), y - (20 / 2d), z - (20 / 2d), x + (20 / 2d), y + (20 / 2d), z + (20 / 2d)), null);
                                if(!entityList.isEmpty()) {
                                    float[] angles = Utils.getAngles(entityList.get(0));
                                    Minecraft.getMinecraft().thePlayer.rotationYaw = angles[0];
                                    Minecraft.getMinecraft().thePlayer.rotationPitch = angles[1];
                                }
                            }
                            if (FunctionManager.getStatus("AutoFish")) {
                                utils.print("已经关闭AutoFish");
                                FunctionManager.setStatus("AutoFish", false);
                            }
                            if (FunctionManager.getStatus("ItemTranslate")) {
                                FunctionManager.setStatus("ItemTranslate", false);
                                TranslateOn = true;
                            } else {
                                TranslateOn = false;
                            }
                            Ticking = true;
                            Tick = 0;
                        }
                    }
                }
            }
            if(event.phase == TickEvent.Phase.END) {
                if (Ticking) {
                    if (!ActiveStaff()) {
                        utils.print("无法找到对应物品");
                    }
                    if (TranslateOn) {
                        FunctionManager.setStatus("ItemTranslate", true);
                    }
                    FunctionManager.setStatus("AutoFish", true);
                    if(aim) {
                        Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
                        Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
                    }
                    if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null){
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    }
                    Ticking = false;
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event){
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                double second = ((double) (((Integer)getConfigByFunctionName.get("AutoKillWorm","cooldown") * 40)) - Tick) / 40;
                HUDManager.Render("Worm Killer Cooldown",(int)second,(Integer)getConfigByFunctionName.get("AutoKillWorm","timerX"),(Integer)getConfigByFunctionName.get("AutoKillWorm","timerY"));
            }
        }
    }

    public boolean ActiveStaff() {
        boolean foundStaff = false;
        if (System.currentTimeMillis() - latest >= 0) {
            latest = System.currentTimeMillis();
            for (int i = 0; i < 8; ++i) {
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains((String)getConfigByFunctionName.get("AutoKillWorm","itemName"))) {
                    int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, stack);
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = currentSlot;
                    foundStaff = true;
                    break;
                }
            }
        }
        return foundStaff;
    }
    @SubscribeEvent
    public void WorldChangeTrigger(ClientChatReceivedEvent event) {
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("You are playing on profile")) {
                new Utils().print("检测到世界服务器改变,自动关闭AutoWormKill");
                FunctionManager.setStatus("AutoKillWorm",false);
            }
        }
    }

}
