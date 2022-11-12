package com.greencat.antimony.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Chroma {
    private final int Largest = 16777215;
    public static int CurrentColor = 0;
    private boolean state = false;
    public Chroma() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(!state) {
            if (CurrentColor + 5 > Largest) {
                state = true;
            } else {
                CurrentColor = CurrentColor + 5;
            }
        }
        if(state) {
            if (CurrentColor - 5 < 0) {
                state = false;
            } else {
                CurrentColor = CurrentColor - 5;
            }
        }
    }
    public static int getR(){
        try{
           String HexColor =  Utils.Dec2Hex(CurrentColor);
           return Utils.Hex2Dec(HexColor.substring(0,2));
        } catch(Exception e){
            return 0;
        }
    }
    public static int getG(){
        try{
            String HexColor =  Utils.Dec2Hex(CurrentColor);
            return Utils.Hex2Dec(HexColor.substring(2,4));
        } catch(Exception e){
            return 0;
        }
    }
    public static int getB(){
        try{
            String HexColor =  Utils.Dec2Hex(CurrentColor);
            return Utils.Hex2Dec(HexColor.substring(4,6));
        } catch(Exception e){
            return 0;
        }
    }
}