package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.common.config.ConfigLoader;
import com.GreenCat.common.event.CustomEventHandler;
import com.GreenCat.utils.Utils;
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

import java.util.Iterator;
import java.util.List;

public class CustomPetName {
    Utils utils = new Utils();
    String OriginalName = null;
    String PetName;
    public CustomPetName() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void AutoEnableFunction(ClientChatReceivedEvent event){
        if(EnumChatFormatting.func_110646_a(event.message.func_150254_d()).contains("Welcome") && EnumChatFormatting.func_110646_a(event.message.func_150254_d()).contains("Hypixel SkyBlock")){
            if(FunctionManager.getStatus("CustomPetNameTag")){
                FunctionManager.setStatus("CustomPetNameTag",false);
                FunctionManager.setStatus("CustomPetNameTag",true);
            }
            if(!FunctionManager.getStatus("CustomPetNameTag")){
                FunctionManager.setStatus("CustomPetNameTag",true);
            }
            utils.print("检测到加入SkyBlock,已经自动开启CustomPetNameTag");
        }
        if(EnumChatFormatting.func_110646_a(event.message.func_150254_d()).contains("You summoned your")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(FunctionManager.getStatus("CustomPetNameTag"))
                    {
                        FunctionManager.setStatus("CustomPetNameTag", false);
                        OriginalName = null;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FunctionManager.setStatus("CustomPetNameTag", true);
                        utils.print("检测到更换宠物,已经自动重载CustomPetNameTag");
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        if(EnumChatFormatting.func_110646_a(event.message.func_150254_d()).contains("Autopet equipped your")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(FunctionManager.getStatus("CustomPetNameTag"))
                    {
                        FunctionManager.setStatus("CustomPetNameTag", false);
                        OriginalName = null;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FunctionManager.setStatus("CustomPetNameTag", true);
                        utils.print("检测到更换宠物,已经自动重载CustomPetNameTag");
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
    @SubscribeEvent
    public void SwitchFunction(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("CustomPetNameTag")) {
            boolean notFoundPet = true;
            if (event.status) {
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntityArmorStand> entities = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for(EntityArmorStand entity : entities){
                    if (EnumChatFormatting.func_110646_a(entity.func_95999_t()).contains(EnumChatFormatting.func_110646_a(Minecraft.func_71410_x().field_71439_g.func_70005_c_() + "'s")) && (!entity.func_95999_t().contains("❤"))) {
                        OriginalName = entity.func_95999_t();
                        notFoundPet = false;
                        break;
                    }
                }
                if(notFoundPet){
                    event.setCanceled(true);
                    utils.print("无法找到宠物,功能已停用");
                }

            } else {
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntityArmorStand> entities = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for(EntityArmorStand entity : entities){
                    if (EnumChatFormatting.func_110646_a(entity.func_95999_t()).contains(EnumChatFormatting.func_110646_a(Minecraft.func_71410_x().field_71439_g.func_70005_c_() + "'s")) && (!entity.func_95999_t().contains("❤"))) {
                        entity.func_96094_a(OriginalName);
                        break;
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void EnableFunction(CustomEventHandler.FunctionEnableEvent event){
        if(Minecraft.func_71410_x().field_71441_e != null) {
            if (event.function.getName().equals("CustomPetNameTag")) {
                boolean notFoundPet = true;
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntityArmorStand> entities = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for(EntityArmorStand entity : entities){
                    if (EnumChatFormatting.func_110646_a(entity.func_95999_t()).contains(EnumChatFormatting.func_110646_a(Minecraft.func_71410_x().field_71439_g.func_70005_c_() + "'s")) && (!entity.func_95999_t().contains("❤"))) {
                        OriginalName = entity.func_95999_t();
                        notFoundPet = false;
                        break;
                    }
                }
                if(notFoundPet){
                    event.setCanceled(true);
                    utils.print("无法找到宠物,功能已停用");
                }
            }
        }
    }
    @SubscribeEvent
    public void DisableFunction(CustomEventHandler.FunctionDisabledEvent event){
        if(Minecraft.func_71410_x().field_71441_e != null) {
            if (event.function.getName().equals("CustomPetNameTag")) {
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntityArmorStand> entities = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityArmorStand.class, new AxisAlignedBB(x - 200, y - 200, z - 200, x + 200, y + 200, z + 200), null);
                for(EntityArmorStand entity : entities){
                    if (EnumChatFormatting.func_110646_a(entity.func_95999_t()).contains(EnumChatFormatting.func_110646_a(Minecraft.func_71410_x().field_71439_g.func_70005_c_() + "'s"))) {
                        entity.func_96094_a(OriginalName);
                        break;
                    }
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyPetName(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Minecraft.func_71410_x().field_71441_e != null) {
            if (FunctionManager.getStatus("CustomPetNameTag")) {
                Entity entity = event.entity;
                if (entity instanceof EntityArmorStand) {
                    if (entity.func_145818_k_()) {
                        if (entity.func_95999_t().contains(Minecraft.func_71410_x().field_71439_g.getDisplayNameString())) {
                            if(OriginalName != null) {
                                PetName = OriginalName;
                                if (ConfigLoader.getCustomPetLevel() != 0) {
                                    String[] Temp1 = PetName.split("]");
                                    String[] Temp2 = Temp1[0].split("\\[");
                                    PetName = Temp2[0] + "[" + EnumChatFormatting.GRAY + "Lv" + ConfigLoader.getCustomPetLevel() + EnumChatFormatting.DARK_GRAY + "]" + Temp1[1];
                                }
                                String[] rules = ConfigLoader.getPetNameRule();
                                for (String rule : rules) {
                                    String[] key2value = rule.split("=");
                                    PetName = PetName.replace(key2value[0], key2value[1]);
                                }
                                entity.func_96094_a(PetName);
                            }


                        }
                    }
                }
            }
        }
    }
}
