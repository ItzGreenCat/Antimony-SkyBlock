package com.greencat.common.event;

import com.greencat.type.AntimonyFunction;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CustomEventHandler {
    public static final EventBus EVENT_BUS = new EventBus();
    @Cancelable
    public static class FunctionEnableEvent extends Event {
        public AntimonyFunction function;
        public FunctionEnableEvent(AntimonyFunction function){
            this.function = function;
        }
    }
    @Cancelable
    public static class FunctionDisabledEvent extends Event {
        public AntimonyFunction function;
        public FunctionDisabledEvent(AntimonyFunction function){
            this.function = function;
        }
    }
    @Cancelable
    public static class FunctionSwitchEvent extends Event {
        public AntimonyFunction function;
        public boolean status;
        public FunctionSwitchEvent(AntimonyFunction function, boolean status){
            this.function = function;
            this.status = status;
        }
    }
    public static class ClientTickEndEvent extends Event {

        private static int staticCount = 0;
        public int count;

        public ClientTickEndEvent() {
            count = staticCount;
        }

        public boolean every(int ticks) {
            return count % ticks == 0;
        }

        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent event) {
            if(event.phase == TickEvent.Phase.END) {
                MinecraftForge.EVENT_BUS.post(new ClientTickEndEvent());
                staticCount++;
            }
        }

    }
    public static class PacketReceivedEvent extends Event {
        public Packet packet;
        public ChannelHandlerContext context;

        public PacketReceivedEvent(Packet packet,ChannelHandlerContext context) {
            this.packet = packet;
            this.context = context;
        }
    }

}
