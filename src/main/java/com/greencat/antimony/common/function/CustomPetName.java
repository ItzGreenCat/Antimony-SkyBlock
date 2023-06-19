package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.notice.Notice;
import com.greencat.antimony.core.notice.NoticeManager;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.annotation.EventModule;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class CustomPetName {
    String OriginalName = null;
    String PetName;

    public CustomPetName() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void AutoEnableFunction(ClientChatReceivedEvent event) {
        if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("Welcome") && EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("Hypixel SkyBlock")) {
            if (FunctionManager.getStatus("CustomPetNameTag")) {
                FunctionManager.setStatus("CustomPetNameTag", false);
                FunctionManager.setStatus("CustomPetNameTag", true);
            }
            if (!FunctionManager.getStatus("CustomPetNameTag")) {
                FunctionManager.setStatus("CustomPetNameTag", true);
            }
            Utils.print("检测到加入SkyBlock,已经自动开启CustomPetNameTag");
            Notice notice = new Notice("Join SkyBlock Detected","CustomPetName has been","automatically opened");
            NoticeManager.add(notice);
        }
        if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("You summoned your")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (FunctionManager.getStatus("CustomPetNameTag")) {
                        FunctionManager.setStatus("CustomPetNameTag", false);
                        OriginalName = null;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FunctionManager.setStatus("CustomPetNameTag", true);
                        Utils.print("检测到更换宠物,已经自动重载CustomPetNameTag");
                        Notice notice = new Notice("Pet Replacement Detected","CustomPetName has been","automatically overloaded");
                        NoticeManager.add(notice);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("Autopet equipped your")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (FunctionManager.getStatus("CustomPetNameTag")) {
                        FunctionManager.setStatus("CustomPetNameTag", false);
                        OriginalName = null;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FunctionManager.setStatus("CustomPetNameTag", true);
                        Utils.print("检测到更换宠物,已经自动重载CustomPetNameTag");
                        Notice notice = new Notice("Pet Replacement Detected","CustomPetName has been","automatically overloaded");
                        NoticeManager.add(notice);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    @EventModule
    public void SwitchFunction(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("CustomPetNameTag")) {
            boolean notFoundPet = true;
            if (event.status) {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for (EntityArmorStand entity : entities) {
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag()).contains(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getName() + "'s")) && (!entity.getCustomNameTag().contains("❤"))) {
                        OriginalName = entity.getCustomNameTag();
                        notFoundPet = false;
                        break;
                    }
                }
                if (notFoundPet) {
                    event.setCanceled(true);
                    Utils.print("无法找到宠物,功能已停用");
                }

            } else {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for (EntityArmorStand entity : entities) {
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag()).contains(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getName() + "'s")) && (!entity.getCustomNameTag().contains("❤"))) {
                        entity.setCustomNameTag(OriginalName);
                        break;
                    }
                }
            }
        }
    }

    @EventModule
    public void EnableFunction(CustomEventHandler.FunctionEnableEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (event.function.getName().equals("CustomPetNameTag")) {
                boolean notFoundPet = true;
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for (EntityArmorStand entity : entities) {
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag()).contains(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getName() + "'s")) && (!entity.getCustomNameTag().contains("❤"))) {
                        OriginalName = entity.getCustomNameTag();
                        notFoundPet = false;
                        break;
                    }
                }
                if (notFoundPet) {
                    event.setCanceled(true);
                    Utils.print("无法找到宠物,功能已停用");
                }
            }
        }
    }

    @EventModule
    public void DisableFunction(CustomEventHandler.FunctionDisabledEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (event.function.getName().equals("CustomPetNameTag")) {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for (EntityArmorStand entity : entities) {
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag()).contains(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getName() + "'s"))) {
                        entity.setCustomNameTag(OriginalName);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyPetName(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            try {
                if (FunctionManager.getStatus("CustomPetNameTag")) {
                    Entity entity = event.entity;
                    if (entity instanceof EntityArmorStand) {
                        if (entity.hasCustomName()) {
                            if (entity.getCustomNameTag().contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {
                                if (OriginalName != null) {
                                    PetName = OriginalName;
                                    if ((Integer) ConfigInterface.get("CustomPetNameTag", "petLevel") != 0) {
                                        String[] Temp1 = PetName.split("]");
                                        String[] Temp2 = Temp1[0].split("\\[");
                                        PetName = Temp2[0] + "[" + EnumChatFormatting.GRAY + "Lv" + (Integer) ConfigInterface.get("CustomPetNameTag", "petLevel") + EnumChatFormatting.DARK_GRAY + "]" + Temp1[1];
                                    }
                                    String ruleStr = (String) ConfigInterface.get("CustomPetNameTag", "petName");
                                    if(!ruleStr.equalsIgnoreCase("null")){
                                        String[] rules = ruleStr.split(",");
                                        for (String rule : rules) {
                                            String[] key2value = rule.split("=");
                                            PetName = PetName.replace(key2value[0], key2value[1]);
                                        }
                                        entity.setCustomNameTag(PetName);
                                    }
                                }


                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
