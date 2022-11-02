package com.greencat.common.register;

import com.greencat.common.storage.SelectGUIStorage;
import com.greencat.type.AntimonyFunction;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.FontManager;

import java.util.ArrayList;
import java.util.List;

public class AntimonyRegister {
    public static List<AntimonyFunction> FunctionList = new ArrayList<AntimonyFunction>();
    SelectGUIStorage SelectGuiStorage = new SelectGUIStorage();
    public void RegisterFunction(AntimonyFunction function) {
        FunctionList.add(function);
    }
    public static void ReList(){
        List<AntimonyFunction> originalList =FunctionList;
        AntimonyFunction originalArray[]=originalList.toArray(new AntimonyFunction[0]);
        for (int i = 0; i < originalArray.length; i++) {
            AntimonyFunction insertValue=originalArray[i];
            int insertIndex=i-1;
            while (insertIndex>=0 && FontManager.GothamRoundedFont.getStringWidth(insertValue.getName()) < FontManager.GothamRoundedFont.getStringWidth(originalArray[insertIndex].getName())) {
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
