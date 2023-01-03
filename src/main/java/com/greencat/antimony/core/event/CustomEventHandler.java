package com.greencat.antimony.core.event;

import com.greencat.antimony.core.type.AntimonyFunction;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class CustomEventHandler {
    public static final EventBus EVENT_BUS = new EventBus();
    //trigger on a function enable
    @Cancelable
    public static class FunctionEnableEvent extends Event {
        public AntimonyFunction function;
        public FunctionEnableEvent(AntimonyFunction function){
            this.function = function;
        }
    }
    //trigger on a function disable
    @Cancelable
    public static class FunctionDisabledEvent extends Event {
        public AntimonyFunction function;
        public FunctionDisabledEvent(AntimonyFunction function){
            this.function = function;
        }
    }
    //trigger on a function switching state
    @Cancelable
    public static class FunctionSwitchEvent extends Event {
        public AntimonyFunction function;
        public boolean status;
        public FunctionSwitchEvent(AntimonyFunction function, boolean status){
            this.function = function;
            this.status = status;
        }
    }
    @Deprecated
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
    //trigger on received a packet from server
    @Cancelable
    public static class PacketReceivedEvent extends Event {
        public Packet packet;
        public ChannelHandlerContext context;

        public PacketReceivedEvent(Packet packet,ChannelHandlerContext context) {
            this.packet = packet;
            this.context = context;
        }
    }
    //trigger on a player motion changed
    @Cancelable
    public static class MotionChangeEvent extends Event {
        public float yaw;
        public float pitch;
        public double x;
        public double y;
        public double z;
        public boolean onGround;
        public boolean sprinting;
        public boolean sneaking;

        protected MotionChangeEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
            this.sneaking = sneaking;
            this.sprinting = sprinting;
        }

        @Cancelable
        public static class Post extends MotionChangeEvent {
            public Post(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
                super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
            }

            public Post(MotionChangeEvent event) {
                super(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround, event.sprinting, event.sneaking);
            }
        }

        @Cancelable
        public static class Pre extends MotionChangeEvent {
            public Pre(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
                super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
            }
        }
    }
    //trigger on client send a packet to server
    @Cancelable
    public static class PacketSentEvent extends Event {
        public Packet<?> packet;

        public PacketSentEvent(Packet<?> packet) {
            this.packet = packet;
        }

        public static class Post extends Event {
            public Packet<?> packet;

            public Post(Packet<?> packet) {
                this.packet = packet;
            }
        }
    }
    @Cancelable
    public static class PacketEvent extends Event{
        public Packet<?> packet;
        public PacketEvent(Packet<?> packet) {
            this.packet = packet;
        }
    }
    @Deprecated
    @Cancelable
    public static class MoveStrafeEvent extends Event {
        public float strafe;
        public float forward;
        public float friction;

        public MoveStrafeEvent(float strafe,float forward,float friction) {
            this.strafe = strafe;
            this.forward = forward;
            this.friction = friction;
        }
    }
    //trigger on player LivingUpdated
    public static class PlayerUpdateEvent extends Event{
        public PlayerUpdateEvent(){}
    }
    //trigger on a block changed
    public static class BlockChangeEvent extends Event {
        public BlockPos pos;
        public IBlockState state;

        public BlockChangeEvent(BlockPos pos, IBlockState state) {
            this.pos = pos;
            this.state = state;
        }
    }
    //trigger on a custom message received from antimony channel
    public static class CustomChannelReceivedEvent extends Event{
        public int id;
        public List<String> context;
        public CustomChannelReceivedEvent(int packetID,List<String> context) {
            this.id = packetID;
            this.context = context;
        }
    }
    @Cancelable
    public static class RenderTileEntityPreEvent extends Event {
        public TileEntity entity;

        public RenderTileEntityPreEvent(TileEntity entity) {
            this.entity = entity;
        }
    }
}
