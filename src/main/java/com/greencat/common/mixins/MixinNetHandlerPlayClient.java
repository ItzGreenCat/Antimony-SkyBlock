package com.greencat.common.mixins;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.*;

@Mixin(value = {NetHandlerPlayClient.class})
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = {"handleEntityVelocity"}, cancellable = true, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V")})
    public void handleEntityVelocity(S12PacketEntityVelocity s12packetv, CallbackInfo cbi) {
        if (FunctionManager.getStatus("Velocity")) {
            Entity velocityEntity = Minecraft.getMinecraft().theWorld.getEntityByID(s12packetv.getEntityID());
            if (velocityEntity instanceof EntityPlayerSP) {
                        /*boolean isUsingSkyBlockKnockBack = false;
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
                            double motionX = s12packetv.getMotionX();
                            double motionY = s12packetv.getMotionY();
                            double motionZ = s12packetv.getMotionZ();
                            if(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition()).getBlock() == Blocks.lava ||
                            Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-1,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-2,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-3,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-4,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-5,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-6,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-7,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-8,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-9,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-10,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-11,0)).getBlock() == Blocks.lava ||
                                    Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().thePlayer.getPosition().add(0,-12,0)).getBlock() == Blocks.lava) {
                                velocityEntity.setVelocity(Minecraft.getMinecraft().thePlayer.motionX + motionX * 0.01 / 8000.0D, motionY * 1 / 8000.0D, Minecraft.getMinecraft().thePlayer.motionZ + motionZ * 0.01 / 8000.0D);
                            }
                            cbi.cancel();
                        }*/
            }
        }
    }
}