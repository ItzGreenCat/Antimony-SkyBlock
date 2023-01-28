package com.greencat.antimony.utils.login;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TokenUtils {
    public static String getAccessTokenByRefresh(String refresh){
        try {
            String XBoxToken = getXboxAccessToken(refresh);
            String XBLToken = getXBL(XBoxToken);
            Map.Entry<String, String> XSTSToken = getXSTS(XBLToken);
            return getAccessToken(XSTSToken);
        } catch(Exception e){
            return null;
        }
    }
    public static String getXboxAccessToken(String refresh) throws Exception{
        String MicrosoftOAuthDesktopUrl = "https://login.live.com/oauth20_token.srf";
        URL ConnectUrl=new URL(MicrosoftOAuthDesktopUrl);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        String param="client_id=00000000402b5328" +
                "&refresh_token=" +refresh+
                "&grant_type=refresh_token" +
                "&redirect_uri=https://login.live.com/oauth20_desktop.srf" +
                "&scope=service::user.auth.xboxlive.com::MBI_SSL";
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedInputStream reader=new BufferedInputStream(connection.getInputStream());
        byte[] bytes=new byte[1024];
        String context = "";
        while ((reader.read(bytes))>0){
            context = context + new String(bytes);
        }
        String token = context.split("\"access_token\":\"")[1].split("\",\"refresh_token")[0];
        return token;
    }
    public static String getXBL(String XboxToken) throws Exception{
        String XBLUrl = "https://user.auth.xboxlive.com/user/authenticate";
        URL ConnectUrl=new URL(XBLUrl);
        String param=null;
        JSONObject xbl_param=new JSONObject(true);
        JSONObject xbl_properties=new JSONObject(true);
        xbl_properties.put("AuthMethod","RPS");
        xbl_properties.put("SiteName","user.auth.xboxlive.com");
        xbl_properties.put("RpsTicket",XboxToken);
        //here is your access token above
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","http://auth.xboxlive.com");
        xbl_param.put("TokenType","JWT");
        param= JSON.toJSONString(xbl_param);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedInputStream reader=new BufferedInputStream(connection.getInputStream());
        byte[] bytes=new byte[1024];
        String context = "";
        while ((reader.read(bytes))>0){
            context = context + new String(bytes);
        }
        String token = context.split("\"Token\":\"")[1].split("\",\"DisplayClaims")[0];
        return token;
    }
    public static Map.Entry<String,String> getXSTS(String XBL) throws Exception{
        URL ConnectUrl=new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        String param=null;
        List<String> tokens=new ArrayList<>();
        tokens.add(XBL);
        JSONObject xbl_param=new JSONObject(true);
        JSONObject xbl_properties=new JSONObject(true);
        xbl_properties.put("SandboxId","RETAIL");
        xbl_properties.put("UserTokens", JSONArray.parse(JSON.toJSONString(tokens)));
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","rp://api.minecraftservices.com/");
        xbl_param.put("TokenType","JWT");
        param=JSON.toJSONString(xbl_param);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedInputStream reader=new BufferedInputStream(connection.getInputStream());
        byte[] bytes=new byte[1024];
        String context = "";
        while ((reader.read(bytes))>0){
            context = context + new String(bytes);
        }
        String token = context.split("\"Token\":\"")[1].split("\",\"DisplayClaims")[0];
        String uhs = context.split("\"uhs\":\"")[1].split("\"}]}")[0];
        return new AbstractMap.SimpleEntry<String,String>(token,uhs);
    }
    public static String getAccessToken(Map.Entry<String, String> XSTSToken) throws Exception{
        URL ConnectUrl=new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        String param=null;
        JSONObject xbl_param=new JSONObject(true);
        xbl_param.put("identityToken","XBL3.0 x=" + XSTSToken.getValue() + ";" + XSTSToken.getKey());
        param=JSON.toJSONString(xbl_param);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedInputStream reader=new BufferedInputStream(connection.getInputStream());
        byte[] bytes=new byte[1024];
        String context = "";
        while ((reader.read(bytes))>0){
            context = context + new String(bytes);
        }
        String token = context.split("\"access_token\" : \"")[1].split("\",")[0];
        return token;
    }
    public static Map.Entry<String,String> getProfile(String token) throws Exception{
        URL ConnectUrl=new URL("https://api.minecraftservices.com/minecraft/profile");
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + token);
        BufferedInputStream reader=new BufferedInputStream(connection.getInputStream());
        byte[] bytes=new byte[1024];
        String context = "";
        while ((reader.read(bytes))>0){
            context = context + new String(bytes);
        }
        String name = context.split("\"name\" : \"")[1].split("\",")[0];
        String id = context.split("\"id\" : \"")[1].split("\",")[0];
        return new AbstractMap.SimpleEntry<>(name,id);
    }
}
