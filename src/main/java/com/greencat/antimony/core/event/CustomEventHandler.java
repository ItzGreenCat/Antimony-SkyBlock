package com.greencat.antimony.core.event;


import com.greencat.antimony.core.type.AntimonyFunction;
import io.netty.channel.ChannelHandlerContext;
import me.greencat.lwebus.core.EventBus;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.List;

public class CustomEventHandler {
    public static final EventBus EVENT_BUS = new EventBus();
    //trigger on a function enable
    
    public static class FunctionEnableEvent extends Event {
        public AntimonyFunction function;
        public FunctionEnableEvent(AntimonyFunction function){
            this.function = function;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on a function disable
    
    public static class FunctionDisabledEvent extends Event {
        public AntimonyFunction function;
        public FunctionDisabledEvent(AntimonyFunction function){
            this.function = function;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on a function switching state
    
    public static class FunctionSwitchEvent extends Event {
        public AntimonyFunction function;
        public boolean status;
        public FunctionSwitchEvent(AntimonyFunction function, boolean status){
            this.function = function;
            this.status = status;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on received a packet from server
    
    public static class PacketReceivedEvent extends Event {
        public Packet<?> packet;
        public ChannelHandlerContext context;

        public PacketReceivedEvent(Packet packet,ChannelHandlerContext context) {
            this.packet = packet;
            this.context = context;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on a player motion changed
    
    public static class MotionChangeEvent extends Event {
        public float yaw;
        public float pitch;
        public double x;
        public double y;
        public double z;
        public boolean onGround;
        public boolean sprinting;
        public boolean sneaking;
        public boolean pre = false;

        protected MotionChangeEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
            this.sneaking = sneaking;
            this.sprinting = sprinting;
            this.pre = false;
        }
        protected MotionChangeEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking,boolean pre) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
            this.sneaking = sneaking;
            this.sprinting = sprinting;
            this.pre = pre;
        }
        public boolean isPre(){
            return pre;
        }
        
        public static class Post extends MotionChangeEvent {
            @Override
            public boolean isCancelable(){
                return true;
            }
            public Post(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
                super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
            }

            public Post(MotionChangeEvent event) {
                super(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround, event.sprinting, event.sneaking);
            }
        }

        
        public static class Pre extends MotionChangeEvent {
            @Override
            public boolean isCancelable(){
                return true;
            }
            public Pre(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
                super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
            }
            public Pre(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking,boolean pre) {
                super(x, y, z, yaw, pitch, onGround, sprinting, sneaking,pre);
            }
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on client send a packet to server
    
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
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    
    public static class PacketEvent extends Event{
        public Packet<?> packet;
        public PacketEvent(Packet<?> packet) {
            this.packet = packet;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    //trigger on player LivingUpdated
    public static class PlayerUpdateEvent extends Event{
        public PlayerUpdateEvent(){}
        public static class Post extends Event {
            public Post() {

            }
        }
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
    
    public static class RenderTileEntityPreEvent extends Event {
        public TileEntity entity;

        public RenderTileEntityPreEvent(TileEntity entity) {
            this.entity = entity;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
    public static class ClickBlockEvent extends Event{
        public BlockPos pos;
        public EnumFacing facing;
        public ClickBlockEvent(BlockPos pos,EnumFacing facing){
            this.pos = pos;
            this.facing = facing;
        }
    }
    
    public static class GuiContainerEvent extends Event {
        public Container container;
        public GuiScreen gui;
        public Slot slot;
        @Override
        public boolean isCancelable(){
            return true;
        }
        
        public static class DrawSlotEvent extends GuiContainerEvent{
            public DrawSlotEvent(Container c, GuiScreen g, Slot s) {
                container = c;
                gui = g;
                slot = s;
            }
            @Override
            public boolean isCancelable(){
                return true;
            }
        }
        
        public static class SlotClickEvent extends GuiContainerEvent{
            public int slot_id;
            public SlotClickEvent(Container c, GuiScreen g, Slot s,int sid) {
                container = c;
                gui = g;
                slot = s;
                slot_id = sid;
            }
            @Override
            public boolean isCancelable(){
                return true;
            }
        }
    }
    /*public static class AttackEvent extends Event{
        public AttackEvent(){

        }
    }*/
    
    public static class CurrentPlayerMoveEvent extends Event {
        private double x;
        private double y;
        private double z;

        public CurrentPlayerMoveEvent setY(final double y) {
            this.y = y;
            return this;
        }

        public CurrentPlayerMoveEvent setX(final double x) {
            this.x = x;
            return this;
        }

        public CurrentPlayerMoveEvent setZ(final double z) {
            this.z = z;
            return this;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        public CurrentPlayerMoveEvent stop() {
            return this.setMotion(0.0, 0.0, 0.0);
        }

        public CurrentPlayerMoveEvent setMotion(final double x, final double y, final double z) {
            this.x = x;
            this.z = z;
            this.y = y;
            return this;
        }

        public CurrentPlayerMoveEvent(final double x, final double y, final double z) {
            this.x = x;
            this.z = z;
            this.y = y;
        }
    }
    public static class PreAttackEvent extends Event
    {
        public Entity entity;

        public PreAttackEvent(Entity entity) {
            this.entity = entity;
        }
    }
    public static class ScreenRender2DEvent extends Event
    {
        public float partialTicks;

        public ScreenRender2DEvent(float partialTicks) {
            this.partialTicks = partialTicks;
        }
    }
    
    public static class BlockBoundsEvent extends Event {
        public AxisAlignedBB aabb;
        public Block block;
        public BlockPos pos;
        public Entity collidingEntity;

        public BlockBoundsEvent( Block block,  AxisAlignedBB aabb, BlockPos pos, Entity collidingEntity) {
            this.aabb = aabb;
            this.block = block;
            this.pos = pos;
            this.collidingEntity = collidingEntity;
        }
        @Override
        public boolean isCancelable(){
            return true;
        }
    }
}
