package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.function.Velocity;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = {NetHandlerPlayClient.class})
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    private
    WorldClient clientWorldController;
    @Inject(method = {"handleEntityVelocity"}, cancellable = true, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V")})
    public void handleEntityVelocity(S12PacketEntityVelocity s12packetv, CallbackInfo cbi) {
        if (FunctionManager.getStatus("Velocity") && !(isInDungeon() && (Boolean) getConfigByFunctionName.get("Velocity","disableInDungeon"))) {
            Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(s12packetv.getEntityID());
            if (entity != null) {
            if (s12packetv.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                        boolean isUsingSkyBlockKnockBack = false;
                        for(String BootsID : Velocity.BootsIDList){
                            if(BootsID.equals(Utils.getSkyBlockCustomItemID(Utils.getBoots()))){
                                isUsingSkyBlockKnockBack = true;
                                break;
                            }
                        }
                        for(String ItemID : Velocity.ItemIDList){
                            if(ItemID.equals(Utils.getSkyBlockCustomItemID(Utils.getHeldItem()))){
                                isUsingSkyBlockKnockBack = true;
                                break;
                            }
                        }
                        if(!isUsingSkyBlockKnockBack) {
                            if(Minecraft.getMinecraft().thePlayer.isInLava()){
                                entity.setVelocity(0,s12packetv.getMotionY() / 8000.0D,0);
                            } else {
                                cbi.cancel();
                            }
                        }

            }
            }
        }
    }
    private boolean isInDungeon() {
        boolean isInTheCatacombs = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                isInTheCatacombs = true;
            }
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                isInTheCatacombs = true;
            }
        }
        return isInTheCatacombs;
    }
}