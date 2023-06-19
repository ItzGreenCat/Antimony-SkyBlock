package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.gui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class UngrabMouse extends FunctionStatusTrigger {
    public UngrabMouse() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (FunctionManager.getStatus("UngrabMouse")) {
            if (mc.theWorld == null && !isUngrabbed) {
                ungrabMouse();
            }
        }
    }
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (FunctionManager.getStatus("UngrabMouse")) {
            regrabMouse();
        }
    }
    @Override
    public String getName() {
        return "UngrabMouse";
    }

    @Override
    public void post() {
        regrabMouse();
        ClickGUI gui = new ClickGUI(Minecraft.getMinecraft().currentScreen, "root");
        Minecraft.getMinecraft().displayGuiScreen(gui);
        Minecraft.getMinecraft().displayGuiScreen(gui.parentScreen);
    }

    @Override
    public void init() {
        ungrabMouse();
    }
    public static Minecraft mc  = Minecraft.getMinecraft();
    public static boolean isUngrabbed = false;
    public static MouseHelper oldMouseHelper;
    public static boolean doesGameWantUngrab;
    public void ungrabMouse() {
        if (mc.inGameHasFocus && !isUngrabbed) {
            if (oldMouseHelper == null) {
                oldMouseHelper = mc.mouseHelper;
            }

            mc.gameSettings.pauseOnLostFocus = false;
            doesGameWantUngrab = !Mouse.isGrabbed();
            oldMouseHelper.ungrabMouseCursor();
            mc.inGameHasFocus = true;
            /*mc.mouseHelper = new MouseHelper() {
                public void mouseXYChange() {
                }

                public void grabMouseCursor() {
                    doesGameWantUngrab = false;
                }

                public void ungrabMouseCursor() {
                    doesGameWantUngrab = true;
                }
            };*/
            isUngrabbed = true;
        }
    }

    public void regrabMouse() {
        if (isUngrabbed) {
            mc.mouseHelper = oldMouseHelper;
            if (!doesGameWantUngrab) {
                mc.mouseHelper.grabMouseCursor();
            }

            oldMouseHelper = null;
            isUngrabbed = false;
        }
    }
}
