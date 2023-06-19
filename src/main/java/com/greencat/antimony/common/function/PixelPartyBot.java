package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.SmoothRotation;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/***
 * 这个类是Object的子类
 */
public class PixelPartyBot extends FunctionStatusTrigger {
    public PixelPartyBot(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    //boolean isBlank = false;
    boolean waiting = false;
    boolean prevWaiting = false;
    int counter = 0;
    //boolean prevBlank = false;
    BlockPos target = null;

    @Override
    public String getName() {
        return "PixelPartyBot";
    }

    @Override
    public void post() {
        target = null;
        waiting = false;
        counter = 0;
    }

    @Override
    public void init() {
        target = null;
        waiting = false;
        counter = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || !FunctionManager.getStatus("PixelPartyBot")){
            return;
        }
        /*prevBlank = isBlank;
        isBlank = (!isBlank) && Minecraft.getMinecraft().thePlayer.getHeldItem() == null;
        if((isBlank && prevBlank) || (isBlank != prevBlank)){
            return;
        }*/
        boolean temp;
        temp = waiting;
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() == null) {
            counter = 0;
        }
        waiting = !waiting || Minecraft.getMinecraft().thePlayer.getHeldItem() != null;//waiting true,true
        if(waiting && !prevWaiting){
            return;
        }

                if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() != null && Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()) != null) {
                    //if(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down()).getBlock() == Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()) && Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down()).getBlock().getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down())) == Minecraft.getMinecraft().thePlayer.getHeldItem().getMetadata()) {
                    if (target != null && !FunctionManager.getStatus("Pathfinding")) {
                        target = null;
                    } else if (target == null) {
                        target = getTarget();
                        if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() != null && Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()) != null && ((Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down()).getBlock() != Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem())) || (Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down()).getBlock().getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().down())) != Minecraft.getMinecraft().thePlayer.getHeldItem().getMetadata()))) {
                            if (target != null && !FunctionManager.getStatus("Pathfinding")) {
                                if (Minecraft.getMinecraft().thePlayer.getPosition().getX() != target.up().getX() || Minecraft.getMinecraft().thePlayer.getPosition().getY() != target.up().getY() || Minecraft.getMinecraft().thePlayer.getPosition().getZ() != target.up().getZ()) {
                                    Pathfinder.fromPos = Minecraft.getMinecraft().thePlayer.getPosition();
                                    Pathfinder.toPos = target.up();
                                    List<Vec3> temp1 = new ArrayList<>();
                                    temp1.add(Minecraft.getMinecraft().thePlayer.getPositionVector());
                                    temp1.add(new Vec3(target.up()));
                                    Pathfinder.path = temp1;
                                    Pathfinding.noCheck = true;
                                    FunctionManager.setStatus("Pathfinding", true);
                                }
                            }
                        }
                    }
                }
                prevWaiting = (Minecraft.getMinecraft().thePlayer.getHeldItem() == null);
        if(temp && waiting && counter == 0){
            counter++;
        }
        if(temp && waiting && counter == 1){
            prevWaiting = true;
            counter++;
        }
    }
    public BlockPos getTarget(){
        BlockPos nearest = null;
        double distance = 9999;
        for(BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(100,-1,100),Minecraft.getMinecraft().thePlayer.getPosition().add(-100,-1,-100))){
            if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() != null && Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()) != null && Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Block.getBlockFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()) && Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock().getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(pos)) == Minecraft.getMinecraft().thePlayer.getHeldItem().getMetadata()){
                if(nearest == null || Minecraft.getMinecraft().thePlayer.getPosition().distanceSq(pos) < distance){
                    distance = Minecraft.getMinecraft().thePlayer.getPosition().distanceSq(pos);
                    nearest = pos;
                }
            }
        }
        return nearest;
    }
}
