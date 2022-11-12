package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CustomItemSound {
    Utils utils = new Utils();
    public CustomItemSound() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void SoundTrigger(PlayerInteractEvent event) throws FileNotFoundException {
        File CustomSoundDirectory = new File(Antimony.AntimonyDirectory,"CustomSound");
        if(!CustomSoundDirectory.exists()){
            CustomSoundDirectory.mkdir();
        }
        if(FunctionManager.getStatus("CustomItemSound")) {
            File[] fileList = CustomSoundDirectory.listFiles();
            if (!(fileList == null || fileList.length == 0)) {
                for (File configFile : fileList) {
                    String fileName = configFile.getName();
                    if(fileName.lastIndexOf(".")!=-1) {
                        String FileExtraName = fileName.substring(fileName.lastIndexOf(".")).replace(".","");
                        if(FileExtraName.equals("properties")){
                        List<String> contentList = new ArrayList<String>();
                        Scanner sc = new Scanner(new FileReader(configFile));
                        while (sc.hasNextLine()) {
                            String line = sc.nextLine();
                            contentList.add(line);
                        }
                        for (String content : contentList) {
                            String[] K4V = content.split("=");
                            try {
                                if (K4V[0].equals("name")) {
                                    if (EnumChatFormatting.getTextWithoutFormattingCodes(StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem).getDisplayName())).contains(K4V[1])) {
                                        for (String contentMode : contentList) {
                                            String[] K4VMode = contentMode.split("=");
                                            if (K4VMode[0].equals("mode")) {
                                                if (K4VMode[1].equals("single")) {
                                                    for (String contentAction : contentList) {
                                                        String[] K4VAction = contentAction.split("=");
                                                        if (K4VAction[0].equals("action")) {
                                                            if ((K4VAction[1].equals("left") && event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
                                                                for (String contentFile : contentList) {
                                                                    String[] K4VFile = contentFile.split("=");
                                                                    if (K4VFile[0].equals("sound")) {
                                                                        File file = new File(CustomSoundDirectory, K4VFile[1]);
                                                                        playSound(file);
                                                                    }
                                                                }
                                                            }
                                                            if ((K4VAction[1].equals("right") && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) || (K4VAction[1].equals("right") && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
                                                                for (String contentFile : contentList) {
                                                                    String[] K4VFile = contentFile.split("=");
                                                                    if (K4VFile[0].equals("sound")) {
                                                                        File file = new File(CustomSoundDirectory, K4VFile[1]);
                                                                        playSound(file);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (K4VMode[1].equals("random")) {
                                                    for (String contentAction : contentList) {
                                                        String[] K4VAction = contentAction.split("=");
                                                        if (K4VAction[0].equals("action")) {
                                                            if ((K4VAction[1].equals("left") && event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
                                                                for (String contentFile : contentList) {
                                                                    String[] K4VFile = contentFile.split("=");
                                                                    if (K4VFile[0].equals("sound")) {
                                                                        String[] soundList = K4VFile[1].split(",");
                                                                        Random random = new Random();
                                                                        File file = new File(CustomSoundDirectory, soundList[random.nextInt(soundList.length)]);
                                                                        playSound(file);
                                                                    }
                                                                }
                                                            }
                                                            if ((K4VAction[1].equals("right") && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) || (K4VAction[1].equals("right") && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
                                                                for (String contentFile : contentList) {
                                                                    String[] K4VFile = contentFile.split("=");
                                                                    if (K4VFile[0].equals("sound")) {
                                                                        String[] soundList = K4VFile[1].split(",");
                                                                        Random random = new Random();
                                                                        File file = new File(CustomSoundDirectory, soundList[random.nextInt(soundList.length)]);
                                                                        playSound(file);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                utils.print("CustomItemSound:配置读取出错,在" + configFile.getName());
                                utils.print(e.toString());
                                utils.print("取消事件执行");
                            } catch (NullPointerException ignored) {

                            }

                        }
                    }
                }
                }
            }
        }

    }
    @SubscribeEvent
    public void PlayerAttack(AttackEntityEvent event) throws FileNotFoundException {
        File CustomSoundDirectory = new File(Antimony.AntimonyDirectory,"CustomSound");
        if(FunctionManager.getStatus("CustomItemSound")) {
            File[] fileList = CustomSoundDirectory.listFiles();
            if (!(fileList == null || fileList.length == 0)) {
                for (File configFile : fileList) {
                    String fileName = configFile.getName();
                    if(fileName.lastIndexOf(".")!=-1) {
                        String FileExtraName = fileName.substring(fileName.lastIndexOf(".")).replace(".","");
                        if(FileExtraName.equals("properties")){
                    List<String> contentList = new ArrayList<String>();
                    Scanner sc = new Scanner(new FileReader(configFile));
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        contentList.add(line);
                    }
                    for (String content : contentList) {
                        String[] K4V = content.split("=");
                        try {
                            if (K4V[0].equals("name")) {
                                if (EnumChatFormatting.getTextWithoutFormattingCodes(StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem).getDisplayName())).contains(K4V[1])) {
                                    for (String contentMode : contentList) {
                                        String[] K4VMode = contentMode.split("=");
                                        if (K4VMode[0].equals("mode")) {
                                            if (K4VMode[1].equals("single")) {
                                                for (String contentAction : contentList) {
                                                    String[] K4VAction = contentAction.split("=");
                                                    if (K4VAction[0].equals("action")) {
                                                        if (K4VAction[1].equals("left")) {
                                                            for (String contentFile : contentList) {
                                                                String[] K4VFile = contentFile.split("=");
                                                                if (K4VFile[0].equals("sound")) {
                                                                    File file = new File(CustomSoundDirectory, K4VFile[1]);
                                                                    playSound(file);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (K4VMode[1].equals("random")) {
                                                for (String contentAction : contentList) {
                                                    String[] K4VAction = contentAction.split("=");
                                                    if (K4VAction[0].equals("action")) {
                                                        if (K4VAction[1].equals("left")) {
                                                            for (String contentFile : contentList) {
                                                                String[] K4VFile = contentFile.split("=");
                                                                if (K4VFile[0].equals("sound")) {
                                                                    String[] soundList = K4VFile[1].split(",");
                                                                    Random random = new Random();
                                                                    File file = new File(CustomSoundDirectory, soundList[random.nextInt(soundList.length)]);
                                                                    playSound(file);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            utils.print("CustomItemSound:配置读取出错,在" + configFile.getName());
                            utils.print(e.toString());
                            utils.print("取消事件执行");
                        } catch (NullPointerException ignored) {

                        }

                    }
                }
            }
                }
            }
        }
    }
    private void playSound(final File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch(Exception ex) {
                    utils.devLog("Error with playing sound.");
                    utils.devLog("文件名称:" + file.getName());
                    utils.devLog("文件路径:" + file.getPath());
                    ex.printStackTrace();
                }
            }
        }
        ).start();
    }
}
