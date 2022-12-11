package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.utils.EasyReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InstantSwitch {
    public static long latest;
    public InstantSwitch() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void InputTrigger(TickEvent.ClientTickEvent event) {
        try {
            if (Keyboard.isKeyDown(KeyLoader.InstantSwitch.getKeyCode())) {
                if (FunctionManager.getStatus("InstantSwitch") && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainer)) {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        InstantSwitchCore();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void InstantSwitchCore(){
        try {
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                for (int i = 0; i < 8; ++i) {
                    ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                    if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(((String)getConfigByFunctionName.get("InstantSwitch","itemName")).toLowerCase())) {
                        int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                        Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                        if (!(Boolean)getConfigByFunctionName.get("InstantSwitch","leftClick")) {
                            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, stack);
                        } else {
                            EasyReflection UpdateControllerReflect = new EasyReflection(PlayerControllerMP.class, "func_78750_j", new Class[0]);
                            UpdateControllerReflect.invoke(Minecraft.getMinecraft().playerController);
                            EasyReflection InvokeLeftClick = new EasyReflection(Minecraft.class, "func_147116_af", new Class[0]);
                            InvokeLeftClick.invoke(Minecraft.getMinecraft());
                        }

                        Minecraft.getMinecraft().thePlayer.inventory.currentItem = currentSlot;
                        break;
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
