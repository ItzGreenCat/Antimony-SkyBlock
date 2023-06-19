package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.EtherwarpTeleport;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ActionBot extends FunctionStatusTrigger {
    public ActionBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || !FunctionManager.getStatus("ActionBot") || EtherwarpTeleport.position.isEmpty()){
            return;
        }
        Pathfinding.noCheck = true;
        Pathfinding.blockTarget = true;
        if(!FunctionManager.getStatus("Pathfinding")){
            List<Vec3> pos = new ArrayList<>();
            Pathfinder.fromPos = EtherwarpTeleport.position.get(0);
            Pathfinder.toPos = EtherwarpTeleport.position.get(EtherwarpTeleport.position.size() - 1);
            for(BlockPos coord : EtherwarpTeleport.position){
                pos.add(new Vec3(coord.up()));
            }
            Pathfinder.path = pos;
            FunctionManager.setStatus("Pathfinding",true);
        }
    }

    @Override
    public String getName() {
        return "ActionBot";
    }

    @Override
    public void post() {
        FunctionManager.setStatus("Pathfinding",false);
        Pathfinding.noCheck = false;
        if(Pathfinder.path != null) {
            Pathfinder.path.clear();
        }
    }

    @Override
    public void init() {

    }
}
