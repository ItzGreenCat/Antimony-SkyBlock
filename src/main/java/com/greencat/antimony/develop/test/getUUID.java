package com.greencat.antimony.develop.test;

import com.google.gson.Gson;
import com.greencat.antimony.core.type.PlayerInformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getUUID {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "";
                    URL url = new URL("https://playerdb.co/api/player/minecraft/" + "SugarRushIII");
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
                    System.out.println(info.data.player.raw_id);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
