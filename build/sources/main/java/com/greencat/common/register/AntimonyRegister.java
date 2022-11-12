package com.greencat.antimony.core.register;

import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;

import java.util.ArrayList;
import java.util.List;

public class AntimonyRegister {
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
