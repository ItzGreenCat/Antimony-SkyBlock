package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.key.KeyLoader;
import com.greencat.utils.EasyReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InstantSwitch {
    public static long latest;
    public InstantSwitch() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void InputTrigger(TickEvent.ClientTickEvent event) {
        if (Keyboard.isKeyDown(KeyLoader.InstantSwitch.getKeyCode())){
            if (FunctionManager.getStatus("InstantSwitch")) {
                if (System.currentTimeMillis() - latest >= 0) {
                    latest = System.currentTimeMillis();
                    for (int i = 0; i < 8; ++i) {
                        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                        if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(ConfigLoader.getISwitch().split("~~SPLIT~~")[0].toLowerCase())) {
                            int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                            Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                            if (ConfigLoader.getISwitch().split("~~SPLIT~~")[1].equals("RIGHT")) {
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
            }
        }

    }

}
