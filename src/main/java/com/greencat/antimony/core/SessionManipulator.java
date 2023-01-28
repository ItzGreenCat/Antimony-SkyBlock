package com.greencat.antimony.core;

import com.greencat.antimony.common.mixins.SessionAccessor;
import net.minecraft.util.Session;

public class SessionManipulator {
    public static String getToken(Session session){
        return session.getToken();
    }
    public static String getPlayerID(Session session){
        return session.getPlayerID();
    }
    public static String getUsername(Session session){
        return session.getUsername();
    }
    public static void setToken(Session session,String value){
        ((SessionAccessor)session).setToken(value);
    }
    public static void setPlayerID(Session session,String value){
        ((SessionAccessor)session).setID(value);
    }
    public static void setUserName(Session session,String value){
        ((SessionAccessor)session).setName(value);
    }
}
