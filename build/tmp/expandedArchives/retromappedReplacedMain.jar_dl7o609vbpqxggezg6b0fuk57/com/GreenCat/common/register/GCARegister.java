package com.GreenCat.common.register;

import com.GreenCat.common.storage.SelectGUIStorage;
import com.GreenCat.type.AntimonyFunction;
import com.GreenCat.type.SelectObject;
import com.GreenCat.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class GCARegister {
    public static List<AntimonyFunction> FunctionList = new ArrayList<AntimonyFunction>();
    SelectGUIStorage SelectGuiStorage = new SelectGUIStorage();
    public void RegisterFunction(AntimonyFunction function) {
        FunctionList.add(function);
    }
    public void RegisterSelectObject(SelectObject object){
        List<SelectTable> NewList = new ArrayList<SelectTable>();
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(object.getParentID())){
                table.add(object);
            }
            NewList.add(table);
            SelectGuiStorage.SetWholeList(NewList);

        }
    }
    public void RegisterTable(SelectTable table) {
        SelectGUIStorage.TableStorage.add(table);
    }
}
