package com.greencat.common.function.rank;

import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

public class RankList {
    public static HashMap<String,String> rankMap = new HashMap<String, String>();
    public RankList() {
        rankMap.put("__GreenCat__", EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.WHITE + "Cat" + EnumChatFormatting.LIGHT_PURPLE + "]" + " 绿猫" + EnumChatFormatting.LIGHT_PURPLE + "GreenCat" + EnumChatFormatting.WHITE);
        rankMap.put("MusicRune", EnumChatFormatting.RED + "[" + EnumChatFormatting.RED + "ADMIN" + EnumChatFormatting.RED + "]" + " Music" + EnumChatFormatting.LIGHT_PURPLE + "Rune" + EnumChatFormatting.RED);
        rankMap.put("FriendlyBear", EnumChatFormatting.AQUA + "[" + EnumChatFormatting.BLACK + "BlackBear" + EnumChatFormatting.AQUA + "]" + " Friendly" + EnumChatFormatting.AQUA + "Bear" + EnumChatFormatting.WHITE);
        rankMap.put("kkxfj09", EnumChatFormatting.AQUA + "[" + EnumChatFormatting.GOLD + "114514" + EnumChatFormatting.AQUA + "]" + " kkxfj" + EnumChatFormatting.AQUA + "09" + EnumChatFormatting.WHITE);
    }
}
