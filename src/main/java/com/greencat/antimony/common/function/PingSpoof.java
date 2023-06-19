package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.settings.SettingLimitInt;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

public class PingSpoof extends FunctionStatusTrigger implements ReflectionlessEventHandler {
    public PingSpoof() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private final Random random = new Random();
    public int max = 1000;
    public int min = 500;
    private boolean c00 = true;
    private boolean c0F = false;
    private boolean c0B = false;
    private boolean c13 = false;
    private boolean c16 = true;
    private double packetLoss = 0.0D;

    private final LinkedList<Packet<INetHandlerPlayServer>> packetBuffer = new LinkedList<>();

    @Override
    public String getName() {
        return "PingSpoof";
    }

    @Override
    public void post() {
        packetBuffer.forEach(Utils::sendPacketNoEvent);
        packetBuffer.clear();
    }

    @Override
    public void init() {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (FunctionManager.getStatus("PingSpoof")) {
            max = (Integer) ConfigInterface.get("PingSpoof","maxPing");
            min = (Integer) ConfigInterface.get("PingSpoof","minPing");
            c00 = (Boolean) ConfigInterface.get("PingSpoof","c00");
            c0F = (Boolean) ConfigInterface.get("PingSpoof","c0f");
            c0B = (Boolean) ConfigInterface.get("PingSpoof","c0b");
            c13 = (Boolean) ConfigInterface.get("PingSpoof","c13");
            c16 = (Boolean) ConfigInterface.get("PingSpoof","c16");
            packetLoss = (Double) ConfigInterface.get("PingSpoof","packetLoss");
            if(min > max){
                AntimonyRegister.FunctionList.entrySet().stream().filter(it -> it.getKey().equals("PingSpoof")).findFirst().ifPresent(it -> it.getValue().getConfigurationList().stream().filter(setting -> setting instanceof SettingLimitInt).forEach(setting -> {if(((SettingLimitInt) setting).ID.equals("min")){((SettingLimitInt) setting).setText(String.valueOf(max));((SettingLimitInt) setting).setValue();}}));
                min = max;
            }
        }
    }
    @Override
    public void invoke(Event event){
        if(event instanceof CustomEventHandler.PacketEvent){
            onPacket((CustomEventHandler.PacketEvent) event);
        }
    }
    public void onPacket(CustomEventHandler.PacketEvent event) {
        if (!FunctionManager.getStatus("PingSpoof")) {
            return;
        }
        Packet<?> packet = event.packet;
        if (((packet instanceof C00PacketKeepAlive && c00) || (packet instanceof C0FPacketConfirmTransaction && c0F) ||
        (packet instanceof C0BPacketEntityAction && c0B) || (packet instanceof C13PacketPlayerAbilities && c13) ||
        (packet instanceof C16PacketClientStatus && c16))) {
            event.setCanceled(true);
            if (packetLoss == 0f || Math.random() > packetLoss) {
                packetBuffer.add((Packet<INetHandlerPlayServer>) packet);
                queuePacket((long) randomDelay(min, max));
            }
        }
    }
    private int randomDelay(int startInclusive,int endExclusive){
        if (endExclusive - startInclusive <= 0) {
           return startInclusive;
        } else {
            return startInclusive + random.nextInt(endExclusive - startInclusive);
        }
    }
    private void queuePacket(Long delay) {
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (FunctionManager.getStatus("PingSpoof")) {
                    Utils.sendPacketNoEvent(packetBuffer.poll());
                }
            }
        }, delay);
    }
}
