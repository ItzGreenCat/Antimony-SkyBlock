package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.HUDManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoKillWorm {
    public static long latest;
    static boolean TranslateOn;
    static boolean aim;
    static int stage;
    Float[] rotations = new Float[2];
    int Tick = 0;
    int rcTick = 0;
    int stageTick = 0;
    int rcCount = 0;
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
                if(stage == 0) {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        if (Tick < ((Integer) getConfigByFunctionName.get("AutoKillWorm", "cooldown") * 20)) {
                            Tick = Tick + 1;
                        } else {
                            if (mc.theWorld != null) {
                                if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null){
                                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                                }
                                aim = (Boolean) getConfigByFunctionName.get("AutoKillWorm", "aim");
                                if (aim) {
                                    rotations[0] = Minecraft.getMinecraft().thePlayer.rotationYaw;
                                    rotations[1] = Minecraft.getMinecraft().thePlayer.rotationPitch;
                                    Double x = Minecraft.getMinecraft().thePlayer.posX;
                                    Double y = Minecraft.getMinecraft().thePlayer.posY;
                                    Double z = Minecraft.getMinecraft().thePlayer.posZ;
                                    List<EntitySilverfish> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntitySilverfish.class, new AxisAlignedBB(x - (20 / 2d), y - (20 / 2d), z - (20 / 2d), x + (20 / 2d), y + (20 / 2d), z + (20 / 2d)), null);
                                    if (!entityList.isEmpty()) {
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
                                stage = 1;
                                rcTick = 0;
                                Tick = 0;
                            }
                        }
                    }
                }
            }
            if(event.phase == TickEvent.Phase.END) {
                if (stage == 1) {
                    if (rcCount < (Integer) getConfigByFunctionName.get("AutoKillWorm", "rcCount")) {
                        if(rcTick > (Integer)getConfigByFunctionName.get("AutoKillWorm", "rcCooldown")) {
                            if (!ActiveStaff()) {
                                utils.print("无法找到对应物品");
                            }
                            rcCount = rcCount + 1;
                            rcTick = 0;
                        } else {
                            rcTick = rcTick + 1;
                        }
                    } else {
                        if (TranslateOn) {
                            FunctionManager.setStatus("ItemTranslate", true);
                        }
                        FunctionManager.setStatus("AutoFish", true);
                        if (aim) {
                            Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
                            Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
                        }
                        rcCount = 0;
                        stage = 2;
                    }
                }
                if(stage == 2){
                    if(stageTick < 10){
                        stageTick = stageTick + 1;
                    } else {
                        stageTick = 0;
                        stage = 3;
                    }
                }
                if (stage == 3) {
                    if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null){
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    }
                    stage = 0;
                }
            }
        }
    }
    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event){
        if(FunctionManager.getStatus("AutoKillWorm")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                double second = ((double) (((Integer)getConfigByFunctionName.get("AutoKillWorm","cooldown") * 20)) - Tick) / 20;
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
                if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(((String)getConfigByFunctionName.get("AutoKillWorm","itemName")).toLowerCase())) {
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
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if(FunctionManager.getStatus("AutoKillWorm")) {
                new Utils().print("检测到世界服务器改变,自动关闭AutoWormKill");
                FunctionManager.setStatus("AutoKillWorm",false);
        }
    }

}
