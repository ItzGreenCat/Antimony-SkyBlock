package com.greencat.antimony.core;

import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerNameFilter {
    private static final String[] filter = new String[]{"Goblin","Ice Walker","Weakling","Frozen Steve","Kalhuiki","CreeperTam","Pitfighter","Sentry","Scarecrow"};
    public static boolean isValid(EntityPlayer player,boolean npcCheck){
        boolean valid = true;
        for(String keyWord : filter){
            if(player.getName().toLowerCase().contains(keyWord.toLowerCase()) || player == Minecraft.getMinecraft().thePlayer){
                valid = false;
                break;
            }
        }
        if(player.isInvisible() && valid) {
            valid = player.getEquipmentInSlot(0) != null || player.getEquipmentInSlot(1) != null || player.getEquipmentInSlot(2) != null || player.getEquipmentInSlot(3) != null || player.getEquipmentInSlot(4) != null;
        }
        if(npcCheck && player != Minecraft.getMinecraft().thePlayer && valid){
            if(Utils.isNPC(player)) {
                valid = false;
            }
        }
        return valid;
    }
}
