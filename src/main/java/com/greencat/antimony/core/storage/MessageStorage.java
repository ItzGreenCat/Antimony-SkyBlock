package com.greencat.antimony.core.storage;

import com.greencat.antimony.core.type.Notice;

import java.util.ArrayList;
import java.util.List;

public class MessageStorage {
    public static List<Notice> StorageList = new ArrayList<Notice>();
    public static void AddNotice(Notice notice){
        StorageList.add(notice);
    }
    public static List getNoticeList(){
        return StorageList;
    }
    public static void setAllList(List<Notice> list){
        StorageList = list;
    }
    public static void deleteFirst(){
        StorageList.remove(0);
    }
}
