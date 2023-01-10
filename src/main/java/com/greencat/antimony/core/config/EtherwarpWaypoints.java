package com.greencat.antimony.core.config;

import com.greencat.Antimony;
import com.greencat.antimony.core.EtherwarpTeleport;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EtherwarpWaypoints {
    private static Configuration config;
    public EtherwarpWaypoints(FMLPreInitializationEvent event) {
        config = new Configuration(new File(Antimony.AntimonyDirectory,"etherwarp_waypoints.cfg"));
        config.load();
    }
    public static void save(){
        try {
            List<String> strList = new ArrayList<String>();
            for(BlockPos pos : EtherwarpTeleport.position){
                strList.add(pos.getX() + "|" + pos.getY() + "|" + pos.getZ());
            }
            config.get(Configuration.CATEGORY_GENERAL, "waypoints",strList.toArray(new String[0]), "Etherwarp Waypoints").set(strList.toArray(new String[0]));
            config.save();
            config.load();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void load(){
        String[] waypoints = config.get(Configuration.CATEGORY_GENERAL, "waypoints",new String[0], "Etherwarp Waypoints").getStringList();
        List<BlockPos> pos = new ArrayList<BlockPos>();
        if(waypoints.length > 0) {
            for (String waypoint : waypoints) {
                String[] splittedPos = waypoint.split("\\|");
                pos.add(new BlockPos(Integer.parseInt(splittedPos[0]), Integer.parseInt(splittedPos[1]), Integer.parseInt(splittedPos[2])));
            }
            EtherwarpTeleport.position.clear();
            EtherwarpTeleport.position.addAll(pos);
        }
        //config.save();
        config.load();
    }
}
