package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.getConfigByFunctionName.get;

import java.util.*;

public class GiftRecipient {
    Queue<EntityArmorStand> giftQueue = new LinkedList<>();
    public GiftRecipient(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("GiftRecipient")){
            if(Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null){
                return;
            }
            if(Minecraft.getMinecraft().thePlayer.ticksExisted % (Integer)get("GiftRecipient","delay") == 0){
                if(!giftQueue.isEmpty()){
                    EntityArmorStand gift = giftQueue.poll();
                    C02PacketUseEntity c02 = new C02PacketUseEntity(gift, C02PacketUseEntity.Action.INTERACT);
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c02);
                } else {
                        Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(Minecraft.getMinecraft().thePlayer.posX - 5, Minecraft.getMinecraft().thePlayer.posY - 5, Minecraft.getMinecraft().thePlayer.posZ - 5, Minecraft.getMinecraft().thePlayer.posX + 5, Minecraft.getMinecraft().thePlayer.posY + 5, Minecraft.getMinecraft().thePlayer.posZ + 5), null).stream().
                                filter(it -> EnumChatFormatting.getTextWithoutFormattingCodes(it.getCustomNameTag()).contains("CLICK TO OPEN")).
                                forEach((Boolean)get("GiftRecipient","onlyName") ? giftQueue::offer :
                                        it -> Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(it.posX - 0.5, it.posY - 0.5, it.posZ - 0.5, it.posX + 0.5, it.posY + 0.5, it.posZ + 0.5), null).stream()
                                                .filter(entity -> entity.getEquipmentInSlot(4) != null).findFirst().ifPresent(giftQueue::offer)
                                );
                }
            }
        }
    }
}
