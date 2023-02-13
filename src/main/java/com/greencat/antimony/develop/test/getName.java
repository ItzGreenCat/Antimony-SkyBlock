package com.greencat.antimony.develop.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getName {
    public static void main(String[] args) {
        try {
            String content = "";
            URL url = new URL("https://gitee.com/origingreencat/antimony-decorate/raw/master/greencatplayername");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String input;
            while ((input = reader.readLine()) != null) {
                content += input;
            }
            reader.close();
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
