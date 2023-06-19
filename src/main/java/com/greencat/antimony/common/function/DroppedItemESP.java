package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.shader.FramebufferShader;
import com.greencat.antimony.utils.shader.shaders.OutlineShader;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class DroppedItemESP implements ReflectionlessEventHandler {
    public DroppedItemESP() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    private static int colorR = 0;
    private static int colorG = 0;
    private static int colorB = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("DroppedItemESP")){
            colorR = (Integer) ConfigInterface.get("DroppedItemESP","colorR");
            colorG = (Integer) ConfigInterface.get("DroppedItemESP","colorG");
            colorB = (Integer) ConfigInterface.get("DroppedItemESP","colorB");
        }
    }
    public void RenderEvent(CustomEventHandler.ScreenRender2DEvent event) {
        if(FunctionManager.getStatus("DroppedItemESP")){
            if(Minecraft.getMinecraft().theWorld != null){
                final FramebufferShader shader = OutlineShader.OUTLINE_SHADER;
                shader.startDraw(((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(it -> it instanceof EntityItem).forEach(it -> Minecraft.getMinecraft().getRenderManager().renderEntityStatic(it,((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks,true));
                shader.stopDraw(new Color(colorR, colorG, colorB), 1.5f, 1f);
            }
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.ScreenRender2DEvent){
            RenderEvent((CustomEventHandler.ScreenRender2DEvent) event);
        }
    }
}
