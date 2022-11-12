package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.EasyReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Random;

public class AutoClicker {
    public AutoClicker(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void AttackTrigger(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AutoClicker")) {
            try{
                if (Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        MovingObjectPosition position = Minecraft.getMinecraft().thePlayer.rayTrace(3.0D, 0);
                        if (position.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                            EasyReflection UpdateControllerReflect = new EasyReflection(PlayerControllerMP.class, "func_78750_j", new Class[0]);
                            UpdateControllerReflect.invoke(Minecraft.getMinecraft().playerController);
                            EasyReflection InvokeLeftClick = new EasyReflection(Minecraft.class, "func_147116_af", new Class[0]);
                            InvokeLeftClick.invoke(Minecraft.getMinecraft());
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
