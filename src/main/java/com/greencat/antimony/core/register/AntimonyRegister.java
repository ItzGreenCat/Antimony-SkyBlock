package com.greencat.antimony.core.register;

import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.FontManager;

import java.util.ArrayList;
import java.util.List;

public class AntimonyRegister {
    //All registered functions are stored
    public static List<AntimonyFunction> FunctionList = new ArrayList<AntimonyFunction>();
    //GUI list storage
    SelectGUIStorage SelectGuiStorage = new SelectGUIStorage();
    //Register a function
    public void RegisterFunction(AntimonyFunction function) {
        FunctionList.add(function);
    }
    //Reorder the list of functions to make the FunctionList more beautiful
    public static void ReList(){
        List<AntimonyFunction> originalList = FunctionList;
        AntimonyFunction originalArray[] = originalList.toArray(new AntimonyFunction[0]);
        for (int i = 0; i < originalArray.length; i++) {
            AntimonyFunction insertValue=originalArray[i];
            int insertIndex=i-1;
            while (insertIndex>=0 && FontManager.QuicksandBoldFont.getStringWidth(insertValue.getName()) < FontManager.QuicksandBoldFont.getStringWidth(originalArray[insertIndex].getName())) {
                originalArray[insertIndex+1]=originalArray[insertIndex];
                insertIndex--;
            }
            originalArray[insertIndex+1]=insertValue;
        }
        List<AntimonyFunction> newList = new ArrayList<>();
        for(int i=originalArray.length - 1; i >= 0; i--){
            newList.add(originalArray[i]);
        }
        FunctionList.clear();
        FunctionList.addAll(newList);
    }
    //Register an object that you can select
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
    //Register a empty list,you still need to invoke RegisterSelectObject to add SelectObject into list
    public void RegisterTable(SelectTable table) {
        SelectGUIStorage.TableStorage.add(table);
    }
}
