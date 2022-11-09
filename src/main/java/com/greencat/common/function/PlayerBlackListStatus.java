package com.greencat.common.function;

import com.google.common.base.Predicate;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.register.AntimonyRegister;
import com.greencat.type.AntimonyFunction;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jrojro728
 */
public class PlayerBlackListStatus {
    private String name;
    private String UUID;
    private String tag;
    private String adduse;
    private String reason;
    private String DungeonBlack;
    private String Dreason;
    private String Rank;
    private String contact;
    private boolean fun;

    Utils utils = new Utils();

    public String getname() { return name; }

    public String getUUID() { return UUID; }

    public String getTag() { return tag; }

    public String getadduse() { return adduse; }

    public String getReason() { return reason; }

    public String getDungeonBlack() { return DungeonBlack; }

    public String getDreason() { return Dreason; }

    public String getRank() { return Rank; }

    public String getContact() { return contact; }

    public boolean isFun() { return fun; }

    public void setname(String name) { this.name = name; }

    public void setUUID(String uuid) { this.UUID = uuid; }

    public void settag(String tag) { this.tag = tag; }

    public void setaddUse(String addUse) { this.adduse = addUse; }

    public void setreason(String reason) { this.reason = reason; }

    public void setDungeonBlack(String dungeonBlack) { this.DungeonBlack = dungeonBlack; }

    public void setDreason(String dungeonBlackReason) { this.Dreason = dungeonBlackReason; }

    public void setRank(String rank) { this.Rank = rank; }

    public void setcontact(String contact) { this.contact = contact; }

    public void setfun(boolean fun) { this.fun = fun; }

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
    private static final String defaultHost = "https://api.scamlist.cn/uuid/";

    public PlayerBlackListStatus() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    /**
     * 此函数类的主逻辑函数,当然你也可以不用自己搞一个也行
     * @param event 触发此函数的事件对象
     */
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {


        if (event.function.getName().equals("PlayerBlackListStatus")) {
            if(event.status){

            }
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if(FunctionManager.getStatus("PlayerBlackListStatus")) {
            if(Minecraft.getMinecraft().theWorld == null) {return;}
            //init
            Predicate<Entity> entityPredicate = input -> true;
            List<EntityPlayer> entityPlayerList = new ArrayList<>();
            List<String> stringList = new ArrayList<>();
            List<PlayerBlackListStatus> playerBlackListStatusList = new ArrayList<>();
            entityPlayerList.clear();

            entityPlayerList.addAll(Minecraft.getMinecraft().theWorld.getPlayers(EntityPlayer.class, entityPredicate));
            for(EntityPlayer entityPlayer : entityPlayerList) {
                stringList.add(sendGet(entityPlayer.getUniqueID().toString().replace("-", "")).toString());
            }
            for (String s : stringList) {
                playerBlackListStatusList.add(decodeJsonToThisClass(s));
            }
            for (PlayerBlackListStatus playerBlackListStatus : playerBlackListStatusList) {
                if(playerBlackListStatus.playerIsInBlackList()) {
                    utils.print("此世界有高能玩家");
                }
            }
        }
    }

    /**
     * 向api查询玩家是否被列在黑名单里面
     * @param uuid 要查询的玩家的uuid字符串
     * @return 读取到的json字符串
     */
    public @NotNull StringBuffer sendGet(String uuid) {
        try {
            HttpURLConnection connection;
            BufferedReader bufferedReader;
            StringBuffer resultBuffer;

            URL url = new URL(defaultHost + uuid);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                resultBuffer = new StringBuffer();
                String line;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

                while ((line = bufferedReader.readLine()) != null) {
                    resultBuffer.append(line);
                }
                return resultBuffer;
            }
            return new StringBuffer("error");
        } catch (Exception e) {
            e.printStackTrace();
            return new StringBuffer("error");
        }
    }

    /**
     * 如函数名
     * @return 玩家是否是高能玩家
     */
    public boolean playerIsInBlackList() {
        if (getUUID().indexOf(UUID) != 32) { return false; }
        if (isFun()) { return false; }
        return !reason.isEmpty();
    }

    /**
     * 如函数名
     * @param Json 要解码的Json字符串
     * @return 解码到的此对象
     */
    public PlayerBlackListStatus decodeJsonToThisClass(String Json) {
        return Utils.decodeJsonToBean(Json, PlayerBlackListStatus.class);
    }
}
