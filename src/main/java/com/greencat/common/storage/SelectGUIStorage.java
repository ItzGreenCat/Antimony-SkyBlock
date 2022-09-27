package com.greencat.common.storage;

import com.greencat.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class SelectGUIStorage {
    public static List<SelectTable> TableStorage = new ArrayList<SelectTable>();
    public void SetWholeList(List<SelectTable> table){
        TableStorage = table;
    }
}
