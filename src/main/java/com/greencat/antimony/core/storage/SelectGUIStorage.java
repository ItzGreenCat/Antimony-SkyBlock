package com.greencat.antimony.core.storage;

import com.greencat.antimony.core.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class SelectGUIStorage {
    //All SelectTables are stored
    public static List<SelectTable> TableStorage = new ArrayList<SelectTable>();
    //Replace the entire array (elements in )
    public void SetWholeList(List<SelectTable> table){
        TableStorage.clear();
        TableStorage.addAll(table);
    }
}
