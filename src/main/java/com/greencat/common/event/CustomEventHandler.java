package com.greencat.common.event;

import com.greencat.type.AntimonyFunction;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

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
    public static class PacketReceivedEvent extends Event {
        public Packet packet;
        public ChannelHandlerContext context;

        public PacketReceivedEvent(Packet packet,ChannelHandlerContext context) {
            this.packet = packet;
            this.context = context;
        }
    }

}
