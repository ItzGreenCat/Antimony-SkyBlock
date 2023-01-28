package com.greencat.antimony.common.mixins;

import com.google.gson.Gson;
import com.greencat.Antimony;
import com.greencat.antimony.core.SessionManipulator;
import com.greencat.antimony.core.type.PlayerInformation;
import com.greencat.antimony.core.via.protocol.ProtocolCollection;
import com.greencat.antimony.utils.login.TokenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    GuiTextField tokenLogin;
    GuiTextField refreshTokenLogin;
    GuiButton loginButton;
    GuiTextField tokenName;
    boolean success = false;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void injectInitGui(CallbackInfo ci) {
        buttonList.add(new GuiButton(114514, 5, 38, 98, 20,
                ProtocolCollection.getProtocolById(Antimony.getVersion()).getName()));
        tokenLogin = new GuiTextField(1919810, Minecraft.getMinecraft().fontRendererObj,5, 60, 98, 20);
        refreshTokenLogin = new GuiTextField(1919810, Minecraft.getMinecraft().fontRendererObj,5, 150, 98, 20);
        loginButton = new GuiButton(214876215,5, 172, 98, 20,"登录 | 无状态");
        tokenName = new GuiTextField(1919810, Minecraft.getMinecraft().fontRendererObj,5, 82, 98, 20);
        tokenLogin.setMaxStringLength(114514);
        tokenName.setMaxStringLength(114514);
        refreshTokenLogin.setMaxStringLength(114514);
        tokenLogin.setText(Minecraft.getMinecraft().getSession().getToken());
        tokenName.setText(Minecraft.getMinecraft().getSession().getUsername());
        buttonList.add(loginButton);
        Keyboard.enableRepeatEvents(true);
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void injectActionPerformed(GuiButton p_actionPerformed_1_, CallbackInfo ci) {
        if (p_actionPerformed_1_.id == 114514){
            if(Antimony.versionIndex + 1 > ProtocolCollection.values().length - 1){
                Antimony.versionIndex = 0;
            } else {
                Antimony.versionIndex = Antimony.versionIndex + 1;
            }
            Antimony.setVersion(ProtocolCollection.values()[Antimony.versionIndex].getVersion().getVersion());
        }
        if(p_actionPerformed_1_.id == loginButton.id){
            if(!success) {
                p_actionPerformed_1_.displayString = "登录 | 登入中...";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String token = TokenUtils.getAccessTokenByRefresh(refreshTokenLogin.getText());
                        try {
                            tokenLogin.setText(token);
                            Map.Entry<String, String> profile = TokenUtils.getProfile(token);
                            tokenName.setText(profile.getKey());
                            SessionManipulator.setPlayerID(Minecraft.getMinecraft().getSession(),profile.getValue());
                            success = true;
                            p_actionPerformed_1_.displayString = "登录 | 完成";
                        } catch (Exception e) {
                            success = false;
                        }
                        if(!success){
                            if (!tokenLogin.getText().equals(Antimony.lastLoginAccessToken)) {
                                try {
                                    Map.Entry<String, String> profile = TokenUtils.getProfile(tokenLogin.getText());
                                    tokenName.setText(profile.getKey());
                                    SessionManipulator.setPlayerID(Minecraft.getMinecraft().getSession(),profile.getValue());
                                    p_actionPerformed_1_.displayString = "登录 | 完成";
                                } catch (Exception e) {
                                    p_actionPerformed_1_.displayString = "登录 | Token不可用";
                                }
                            } else {
                                p_actionPerformed_1_.displayString = "登录 | Token不可用";
                            }
                        }
                        success = false;
                        Antimony.lastLoginAccessToken = tokenLogin.getText();
                    }
                }).start();
            }
        }
    }
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void injectDrawScreenHead(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci) {
        for(GuiButton button : this.buttonList){
            if(button.id == 114514){
                button.displayString = ProtocolCollection.getProtocolById(Antimony.getVersion()).getName();
            }
        }
    }
    @Inject(method = "keyTyped", at = @At("HEAD"))
    public void injectKeyTyped(char p_keyTyped_1_, int p_keyTyped_2_, CallbackInfo ci) {
        tokenLogin.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
        tokenName.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
        if(tokenName.isFocused()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String content = "";
                        URL url = new URL("https://playerdb.co/api/player/minecraft/" + tokenName.getText());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String input;
                        while ((input = reader.readLine()) != null) {
                            content += input;
                        }
                        reader.close();
                        Gson gson = new Gson();
                        PlayerInformation info = gson.fromJson(content, PlayerInformation.class);
                        SessionManipulator.setPlayerID(Minecraft.getMinecraft().getSession(),info.data.player.raw_id);
                    } catch(Exception e){

                    }
                }
            }).start();
        }
        refreshTokenLogin.textboxKeyTyped(p_keyTyped_1_,p_keyTyped_2_);
    }
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void injectMouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_, CallbackInfo ci) {
        tokenLogin.mouseClicked(p_mouseClicked_1_,p_mouseClicked_2_,p_mouseClicked_3_);
        tokenName.mouseClicked(p_mouseClicked_1_,p_mouseClicked_2_,p_mouseClicked_3_);
        refreshTokenLogin.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_,p_mouseClicked_3_);
    }
    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    public void injectGuiClosed(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(false);
    }
    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void injectDrawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci) {
        mc.fontRendererObj.drawStringWithShadow("<-- 当前版本",
                104, 44, -1);
        mc.fontRendererObj.drawStringWithShadow("<-- AccessToken登入(会自动填写用户名)",
                104, 66, -1);
        mc.fontRendererObj.drawStringWithShadow("<-- 玩家用户名",
                104, 88, -1);
        mc.fontRendererObj.drawStringWithShadow("玩家ID:" + SessionManipulator.getPlayerID(Minecraft.getMinecraft().getSession()),
                5, 110, -1);
        mc.fontRendererObj.drawStringWithShadow("<-- refreshToken登入(会自动填写AccessToken与用户名)",
                104, 156, -1);
        tokenLogin.drawTextBox();
        tokenName.drawTextBox();
        refreshTokenLogin.drawTextBox();
        SessionManipulator.setToken(Minecraft.getMinecraft().getSession(),tokenLogin.getText());
        SessionManipulator.setUserName(Minecraft.getMinecraft().getSession(),tokenName.getText());
    }
}
