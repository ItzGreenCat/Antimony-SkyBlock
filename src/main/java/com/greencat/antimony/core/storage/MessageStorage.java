package com.greencat.antimony.core.storage;

import com.greencat.antimony.core.type.Notice;

import java.util.ArrayList;
import java.util.List;

public class MessageStorage {
    //current function message
    public static List<Notice> StorageList = new ArrayList<Notice>();
    //add a notice to storage list
    public static void AddNotice(Notice notice){
        StorageList.add(notice);
    }
    @Deprecated
    public static List<Notice> getNoticeList(){
        return StorageList;
    }
    //replace this list(element) into a new list(element)
    public static void setAllList(List<Notice> list){
        StorageList.clear();
        StorageList.addAll(list);
    }
    @Deprecated
    public static void deleteFirst(){
        StorageList.remove(0);
    }
}
