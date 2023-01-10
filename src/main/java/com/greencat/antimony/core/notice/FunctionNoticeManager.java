package com.greencat.antimony.core.notice;

public class FunctionNoticeManager{
    public static void add(String name,Boolean status){
        Notice notice = new Notice(name,status ? "Enable" : "Disable");
        NoticeManager.add(notice);
    }
}
