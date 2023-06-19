package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.shader.FramebufferShader;
import com.greencat.antimony.utils.shader.shaders.GlowShader;
import com.greencat.antimony.utils.shader.shaders.OutlineShader;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

import static com.greencat.antimony.core.PlayerNameFilter.isValid;

public class PlayerESP implements ReflectionlessEventHandler {
    public static int mode = 1;

    public PlayerESP() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            mode = (Integer) ConfigInterface.get("PlayerESP", "mode");
        }
    }
    public void RenderESP(CustomEventHandler.ScreenRender2DEvent event) {
        if (FunctionManager.getStatus("PlayerESP")) {
            if (mode == 2) {
                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                    final FramebufferShader shader = OutlineShader.OUTLINE_SHADER;
                    shader.startDraw(((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                    Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(it -> it instanceof EntityPlayer).filter(it -> it != Minecraft.getMinecraft().thePlayer).filter(it -> isValid((EntityPlayer) it, false)).forEach(
                    it -> {
                        Minecraft.getMinecraft().getRenderManager().renderEntityStatic(it, ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks, true);
                    }
                    );
                    shader.stopDraw(Antimony.Color.AntimonyCyan, 1.5f, 1f);
                }
            }
            if (mode == 3) {
                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                    final FramebufferShader shader = GlowShader.GLOW_SHADER;
                    shader.startDraw(((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                    Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(it -> it instanceof EntityPlayer).filter(it -> it != Minecraft.getMinecraft().thePlayer).filter(it -> isValid((EntityPlayer) it, false)).forEach(
                            it -> {
                                Minecraft.getMinecraft().getRenderManager().renderEntityStatic(it, ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks, true);
                            }
                    );
                    shader.stopDraw(Antimony.Color.AntimonyCyan, 2.3f, 1f);
                }
            }
        }
    }

    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityPlayer> event) {
        if (FunctionManager.getStatus("PlayerESP")) {
            if (mode == 0) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) event.entity;
                        if (player != Minecraft.getMinecraft().thePlayer) {
                            if (isValid((EntityPlayer) event.entity, false)) {
                                Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(), Antimony.Color.AntimonyCyan, false, 3);
                            }
                        }
                    }
                }
            }
            if (mode == 1) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) event.entity;
                        if (player != Minecraft.getMinecraft().thePlayer) {
                            if (isValid((EntityPlayer) event.entity, false)) {
                                Utils.render2DESP(player,Utils.getEntityColor(player));
                            }
                        }
                    }
                }
            }
            GlStateManager.color(1.0F,1.0F,1.0F);
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.ScreenRender2DEvent){
            RenderESP((CustomEventHandler.ScreenRender2DEvent)event);
        }
    }
}
