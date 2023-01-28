package com.greencat.antimony.core.auctionTracker;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AuctionTracker {
    public static final List<AuctionItem> auctions = new ArrayList<>();
    public static long lastGet = 0;
    public static boolean finish = true;
    public static void getAuctions(){
        new Thread(() -> {
            auctions.clear();
            finish = false;
            List<AuctionList> list = new ArrayList<>();
            try {
                int page = 0;
                while(true) {
                    URL url = new URL("https://api.hypixel.net/skyblock/auctions" + "?page=" + page);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    BufferedInputStream reader = new BufferedInputStream(connection.getInputStream());
                    byte[] bytes = new byte[1024];
                    String context = "";
                    while ((reader.read(bytes)) > 0) {
                        context = context + new String(bytes);
                    }
                    Gson gson = new Gson();
                    AuctionList auctionlist = gson.fromJson(context, AuctionList.class);
                    if(!auctionlist.success){
                        break;
                    }
                    list.add(auctionlist);
                    page = page + 1;
                }
            } catch(Exception ignored){

            }
            list.forEach(AuctionTracker::addValidAuctions);
            lastGet = System.currentTimeMillis();
            finish = true;
            System.out.println(auctions.size());
        }).start();
    }
    public static void addValidAuctions(AuctionList auctionlist){
        List<AuctionItem> items = Arrays.asList(auctionlist.auctions);
        items.stream().filter(it -> it.bin && it.start > lastGet).forEach(auctions::add);
    }
    public static void main(String[] args){
        getAuctions();
    }
}