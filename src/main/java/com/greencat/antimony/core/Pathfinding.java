package com.greencat.antimony.core;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.timer.SystemTimer;
import me.greencat.lwebus.core.annotation.EventModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class Pathfinding {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static int stuckTicks = 0;
    private static BlockPos oldPos;
    private static BlockPos curPos;
    private static SystemTimer unstucker;
    private static int unstucktime = 3;
    private static int tick = 0;
    public static boolean noCheck = false;
    public static boolean blockTarget = false;

    public Pathfinding() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @EventModule
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("Pathfinding")){
            if(event.status){
                stuckTicks = 0;
                oldPos = null;
                curPos = null;
                if (!Pathfinder.hasPath()) {
                    Utils.print("Pathfinder无法找到路径");
                    FunctionManager.setStatus("Pathfinding",false);
                } else {
                    Utils.print("Pathfinder路径目标: " + Pathfinder.getGoal());
                }
            } else {
                Pathfinder.path = null;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                noCheck = false;
            }
        }
    }

    @EventModule
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        try {
            if (event.function.getName().equals("Pathfinding")) {
                stuckTicks = 0;
                oldPos = null;
                curPos = null;
                if (!Pathfinder.hasPath()) {
                    Utils.print("Pathfinder无法找到路径");
                    FunctionManager.switchStatus("Pathfinding");
                } else {
                    Utils.print("Pathfinder路径目标: " + Pathfinder.getGoal());
                }
            }
        } catch(Exception ignored){

        }
    }

    @EventModule
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("Pathfinding")) {
            Pathfinder.path = null;
            noCheck = false;
            blockTarget = false;
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat) {
            if (Pathfinder.hasPath()) {
                if (++stuckTicks >= this.unstucktime * 20) {
                    curPos = mc.thePlayer.getPosition();
                    if (oldPos != null && Math.sqrt(curPos.distanceSq(oldPos)) <= 0.1D) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                        unstucker = new SystemTimer();
                        unstucker.reset1();
                        return;
                    }

                    oldPos = curPos;
                    stuckTicks = 0;
                }

                if (unstucker != null && unstucker.hasReached(2000L)) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                    unstucker = null;
                }
                if (Pathfinder.path != null && !Pathfinder.path.isEmpty() && blockTarget && Pathfinder.getCurrent() != null) {
                    Rotation rotation = Utils.getRotation(new BlockPos(Pathfinder.getCurrent()));
                    Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
                }

                Vec3 first = Pathfinder.getCurrent().addVector(0.5D, 0.0D, 0.5D);
                Rotation needed = Utils.getRotation(first);
                needed.setPitch(mc.thePlayer.rotationPitch);
                if (Utils.getHorizontalDistance(mc.thePlayer.getPositionVector(), first) > 0.69D) {
                    tick = tick + 1;
                    if(tick < 200) {
                        if (Utils.done && needed.getYaw() < 135.0F) {
                            Utils.setup(needed, 150L);
                        }

                        Vec3 lastTick;
                        if (Pathfinder.hasNext()) {
                            lastTick = Pathfinder.getNext().addVector(0.5D, 0.0D, 0.5D);
                            double xDiff = Math.abs(Math.abs(lastTick.xCoord) - Math.abs(first.xCoord));
                            double zDiff = Math.abs(Math.abs(lastTick.zCoord) - Math.abs(first.zCoord));
                            mc.thePlayer.setSprinting(xDiff == 1.0D && zDiff == 0.0D || xDiff == 0.0D && zDiff == 1.0D);
                        }

                        lastTick = new Vec3(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
                        Vec3 diffy = mc.thePlayer.getPositionVector().subtract(lastTick);
                        diffy = diffy.addVector(diffy.xCoord * 4.0D, 0.0D, diffy.zCoord * 4.0D);
                        Vec3 nextTick = mc.thePlayer.getPositionVector().add(diffy);
                        Utils.stopMovement();
                        List<KeyBinding> neededPresses = Utils.getNeededKeyPresses(mc.thePlayer.getPositionVector(), first);
                        if (!(Math.abs(nextTick.distanceTo(first) - mc.thePlayer.getPositionVector().distanceTo(first)) > 0.05D) || !(nextTick.distanceTo(first) > mc.thePlayer.getPositionVector().distanceTo(first))) {
                            neededPresses.forEach((v) -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
                        }

                        if (Math.abs(mc.thePlayer.posY - first.yCoord) > 0.5D) {
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), mc.thePlayer.posY < first.yCoord);
                        } else {
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                        }
                    } else {
                        if(!noCheck) {
                            BlockPos fromPos = new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector()));
                            BlockPos toPos = Pathfinder.toPos;
                            Pathfinder.setup(fromPos, toPos, 0.0D);
                            if (Pathfinder.hasPath()) {
                                FunctionManager.setStatus("Pathfinding", true);
                            } else {
                                FunctionManager.setStatus("Pathfinding", false);
                                Utils.print("无法找到去此方块的路径");
                            }
                            tick = 0;
                        }
                    }
                } else {
                    tick = 0;
                    Utils.reset();
                    if (!Pathfinder.goNext()) {
                        Utils.stopMovement();
                    }
                }
                /*if(event.phase == TickEvent.Phase.START){
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                }
                if(event.phase == TickEvent.Phase.END){
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                }*/
            } else if (FunctionManager.getStatus("Pathfinding")) {
                FunctionManager.switchStatus("Pathfinding");
            }

        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Pathfinder.path != null && !Pathfinder.path.isEmpty()) {
            Utils.drawLines(Pathfinder.path, 2.0F, event.partialTicks);
            Vec3 last = ((Vec3)Pathfinder.path.get(Pathfinder.path.size() - 1)).addVector(0.0D, -1.0D, 0.0D);
            Utils.OutlinedBoxWithESP(new BlockPos(last),Chroma.color,false);
        }

        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat) {
            if (!Utils.done) {
                Utils.update();
            }

        }
    }
}