package com.GreenCat.common.storage;

import com.GreenCat.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class SelectGUIStorage {
    public static List<SelectTable> TableStorage = new ArrayList<SelectTable>();
    public void SetWholeList(List<SelectTable> table){
        TableStorage = table;
    }
}
