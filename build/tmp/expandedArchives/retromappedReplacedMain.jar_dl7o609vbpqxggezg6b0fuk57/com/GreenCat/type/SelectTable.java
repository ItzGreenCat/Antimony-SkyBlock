package com.GreenCat.type;

import java.util.ArrayList;
import java.util.List;

public class SelectTable {
    List<SelectObject> ObjectList = new ArrayList<SelectObject>();
    String TableID;
    public SelectTable(String ID){
        TableID = ID;
    }
    public void add(SelectObject Object) {
        ObjectList.add(Object);
    }
    public String getID(){
        return TableID;
    }
    public List<SelectObject> getList(){
        return ObjectList;
    }
}

