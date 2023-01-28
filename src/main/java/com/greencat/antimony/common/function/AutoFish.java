package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.HUDManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class AutoFish {
    public static boolean emberStatus = false;
    static int Tick = 40;
    static int hookTick = -1;
    static int maxHookTick = 920;
    static boolean MoveStatus = false;
    static Boolean AutoFishStatus = false;
    static Boolean slugFish = false;
    static int hookThrownCooldown = 0;
    static boolean nextTickThrow = false;
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    float oldLevel;


    public AutoFish() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    public void init() {
        try {
            if (FunctionManager.getStatus("AutoFish")) {
                if (mc.thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    Tick = 0;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (FunctionManager.getStatus("AutoFish")) {
            Utils.print("检测到世界服务器改变,自动关闭AutoFish");
            FunctionManager.setStatus("AutoFish", false);
        }
    }

    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        oldLevel = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.PLAYERS);
        Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1);
        nextTickThrow = false;
    }

    @SubscribeEvent
    public void onDisabled(CustomEventHandler.FunctionDisabledEvent event) {
        if (event.function.getName().equals("AutoFish")) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
            Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel);
            nextTickThrow = false;
        }
    }

    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("AutoFish")) {
            if (!event.status) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel);
                nextTickThrow = false;
            } else {
                oldLevel = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.PLAYERS);
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1);
                nextTickThrow = false;
            }
        }
    }

    @SubscribeEvent
    public void StartTriggerAutoFish(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!FunctionManager.getStatus("AutoFish")) {
                AutoFishStatus = false;
            } else {
                if ((Boolean) getConfigByFunctionName.get("AutoFish", "sneak")) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld != null) {
            if (Tick < 40) {
                if (Tick == 0) {
                    if (Antimony.AutoFishYawState) {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw + 0.3);
                        mc.thePlayer.rotationPitch = (float) (mc.thePlayer.rotationPitch + 0.3);
                        Antimony.AutoFishYawState = false;
                    } else {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw - 0.3);
                        mc.thePlayer.rotationPitch = (float) (mc.thePlayer.rotationPitch - 0.3);
                        Antimony.AutoFishYawState = true;
                    }
                    try {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                } else if (Tick == 1) {
                    try {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Tick == 29) {
                    if (MoveStatus) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), true);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), true);
                    }
                } else if (Tick == 30) {
                    if (MoveStatus) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), true);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), true);
                    }
                } else if (Tick == 31) {
                    if (MoveStatus) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
                    }
                    MoveStatus = !MoveStatus;
                } else if (Tick == 39) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                }
                Tick = Tick + 1;
            } else {
                Tick = 40;
            }
        }
        if (hookTick + 1 >= maxHookTick) {
            hookTick = -1;
        } else {
            if (hookTick + 1 < maxHookTick && hookTick >= 0) {
                hookTick = hookTick + 1;
            }
        }
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoFish")) {
                if ((Boolean) getConfigByFunctionName.get("AutoFish", "throwHook")) {
                    if (!isHookThrown() && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                        if (hookThrownCooldown + 1 > (Integer) getConfigByFunctionName.get("AutoFish", "throwHookCooldown") * 40) {
                            Utils.print("到达设定时间,自动抛竿");
                            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                            hookThrownCooldown = 0;
                        } else {
                            hookThrownCooldown = hookThrownCooldown + 1;
                        }
                    } else {
                        hookThrownCooldown = 0;
                    }
                }
            }
            if ((Boolean) getConfigByFunctionName.get("AutoFish", "rethrow")) {
                if (hookTick + 1 > (Integer) getConfigByFunctionName.get("AutoFish", "rethrowCooldown") * 40) {
                    if (isHookThrown()) {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                        nextTickThrow = true;
                        init();
                        AutoFishStatus = false;
                        if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                            Utils.print("钓鱼检测状态:关闭");
                        }
                        hookTick = -1;
                    }
                }
            }
            if (nextTickThrow) {
                Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                AutoFishStatus = true;
                hookTick = 0;
                if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                    Utils.print("钓鱼检测状态:开启");
                }
                nextTickThrow = false;
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PlaySoundEvent event) throws AWTException {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoFish")) {
                if (event.name.equals("game.player.swim.splash")) {
                    if (AutoFishStatus) {
                        float x = event.result.getXPosF();
                        float y = event.result.getYPosF();
                        float z = event.result.getZPosF();
                        List<EntityFishHook> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityFishHook.class, new AxisAlignedBB(x - (0.5 / 2d), y - (0.5 / 2d), z - (0.5 / 2d), x + (0.5 / 2d), y + (0.5 / 2d), z + (0.5 / 2d)), null);
                        for (EntityFishHook entity : entities) {
                            if (entity.angler == Minecraft.getMinecraft().thePlayer) {
                                slugFish = (Boolean) getConfigByFunctionName.get("AutoFish", "slug");
                                if (slugFish) {
                                    if (hookTick < 0) {
                                        init();
                                        AutoFishStatus = false;
                                        if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                                            Utils.print("钓鱼检测状态:关闭");
                                        }
                                        hookTick = -1;
                                    }
                                } else {
                                    init();
                                    AutoFishStatus = false;
                                    if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                                        Utils.print("钓鱼检测状态:关闭");
                                    }
                                    hookTick = -1;
                                }
                            }
                        }
                    } else {
                        AutoFishStatus = true;
                        hookTick = 0;
                        if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                            Utils.print("钓鱼检测状态:开启");
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void PLayerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            try {
                if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    if (AutoFishStatus) {
                        AutoFishStatus = false;
                        if ((Boolean) getConfigByFunctionName.get("AutoFish", "message")) {
                            Utils.print("钓鱼检测状态:关闭");
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event) {
        if (FunctionManager.getStatus("AutoFish")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                if ((Boolean) getConfigByFunctionName.get("AutoFish", "timer")) {
                    double second = ((double) (hookTick)) / 40;
                    HUDManager.Render("Hook Thrown Time", (int) second, (Integer) getConfigByFunctionName.get("AutoFish", "timerX"), (Integer) getConfigByFunctionName.get("AutoFish", "timerY"));
                    //mc.fontRendererObj.drawString(NoticeString, (new ScaledResolution(mc).getScaledWidth() / 2) - (mc.fontRendererObj.getStringWidth(NoticeString) / 2), (new ScaledResolution(mc).getScaledHeight() / 2) + 40, Antimony.Color);
                }
            }
        }
    }

    public boolean isHookThrown() {
        if (Minecraft.getMinecraft().theWorld != null) {
            boolean thrown = false;
            List<EntityFishHook> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityFishHook.class, new AxisAlignedBB(Minecraft.getMinecraft().thePlayer.posX - (200 / 2d), Minecraft.getMinecraft().thePlayer.posY - (200 / 2d), Minecraft.getMinecraft().thePlayer.posZ - (200 / 2d), Minecraft.getMinecraft().thePlayer.posX + (200 / 2d), Minecraft.getMinecraft().thePlayer.posY + (200 / 2d), Minecraft.getMinecraft().thePlayer.posZ + (200 / 2d)), null);
            for (EntityFishHook entity : entities) {
                if (entity.angler == Minecraft.getMinecraft().thePlayer) {
                    thrown = true;
                    break;
                }
            }
            return thrown;
        } else {
            return false;
        }
    }
}
