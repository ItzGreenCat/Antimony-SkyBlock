package com.greencat.antimony.core.storage;

import com.greencat.antimony.core.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class SelectGUIStorage {
    public static List<SelectTable> TableStorage = new ArrayList<SelectTable>();
    public void SetWholeList(List<SelectTable> table){
        TableStorage = table;
    }
}
