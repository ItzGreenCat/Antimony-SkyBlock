package com.greencat.antimony.utils.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.ResourceLocation;

public class SoundPlayer {
    public static void play(ResourceLocation location){
        ISound sound = new PositionedSound(location) {{
            volume = 1;
            pitch = 1;
            repeat = false;
            repeatDelay = 0;
            attenuationType = ISound.AttenuationType.NONE;
        }};
        float oldLevel = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.PLAYERS);
        Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel);
    }
}

