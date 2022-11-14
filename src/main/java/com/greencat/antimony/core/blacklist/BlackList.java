package com.greencat.antimony.core.blacklist;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.gui.SettingsGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.util.Objects;

public class BlackList {
	public static Minecraft mc = Minecraft.getMinecraft();

	public BlackList() {
		BlackListManager.registerTimer();
		MinecraftForge.EVENT_BUS.register(this);
		CustomEventHandler.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public void onFunctionDisable(CustomEventHandler.FunctionDisabledEvent event){
		if(event.function.getName().equals("BlackList")){
			event.setCanceled(true);
			Minecraft.getMinecraft().displayGuiScreen(new SettingsGUI(Minecraft.getMinecraft().currentScreen,"BlackList", Objects.requireNonNull(FunctionManager.getFunctionByName("BlackList")).getConfigurationList()));
		}
	}
	@SubscribeEvent
	public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
		if(event.function.getName().equals("BlackList")){
			if(!event.status){
				event.setCanceled(true);
				Minecraft.getMinecraft().displayGuiScreen(new SettingsGUI(Minecraft.getMinecraft().currentScreen,"BlackList", Objects.requireNonNull(FunctionManager.getFunctionByName("BlackList")).getConfigurationList()));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@SubscribeEvent
	public void onRender(RenderLivingEvent.Specials.Pre e) {
		float appendY = Loader.isModLoaded("xiaojiaaddons") ? 0.25F : 0.0F;

		if (e.entity == mc.thePlayer && !Loader.isModLoaded("xiaojiaaddons")) {
			double x = e.x;
			double y = e.y;
			double z = e.z;
			String rank = mc.thePlayer.getName();
			y += mc.fontRendererObj.FONT_HEIGHT * 1.15F * 0.02666667F * 2.0F;
			if (BlackListManager.getIfIsGaoNeng(mc.thePlayer) != null) {
				try {
					Method m = this.getRenderMethod((RenderPlayer) e.renderer);
					m.setAccessible(true);
					m.invoke(e.renderer, e.entity,
							"\u00a7b[" + EnumChatFormatting.WHITE
									+ BlackListManager.getIfIsGaoNeng(mc.thePlayer).getRank().replaceAll("&", "\u00a7")
									+ "\u00a7b]",
							x, y + appendY - 0.25F, z, 64);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			try {
				Method m = this.getRenderMethod((RenderPlayer) e.renderer);
				m.setAccessible(true);
				m.invoke(e.renderer, e.entity, rank, x, y - (mc.thePlayer.isSneaking() ? 0.8F : 0.55F), z, 64);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (e.entity instanceof EntityOtherPlayerMP) {

			if (BlackListManager.getIfIsGaoNeng((EntityOtherPlayerMP) e.entity) == null)
				return;

			if (e.renderer instanceof RenderPlayer) {
				double x = e.x;
				double y = e.y;
				double z = e.z;
				String rank = "\u00a7b[" + EnumChatFormatting.WHITE + BlackListManager
						.getIfIsGaoNeng((EntityOtherPlayerMP) e.entity).getRank().replaceAll("&", "\u00a7")
						+ "\u00a7b]";
				y += mc.fontRendererObj.FONT_HEIGHT * 1.15F * 0.02666667F * 2.0F;

				try {
					Method m = this.getRenderMethod((RenderPlayer) e.renderer);
					m.setAccessible(true);
					m.invoke(e.renderer, e.entity, rank, x, y + appendY - (e.entity.isSneaking() ? 0.25F : 0.0F), z,
							64);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
	}

	private Method getRenderMethod(RenderPlayer rp) throws NoSuchMethodException {
		for (Class<?> c = rp.getClass(); c != null; c = c.getSuperclass()) {
			Method[] dc = c.getDeclaredMethods();
			Method[] dc2 = dc;
			int var5 = dc.length;

			for (int i = 0; i < var5; i++) {
				Method m = dc2[i];
				if (m.getName().equals("renderLivingLabel") || m.getName().equals("func_147906_a")) {
					return m;
				}
			}
		}
		throw new NoSuchMethodException();
	}
}
