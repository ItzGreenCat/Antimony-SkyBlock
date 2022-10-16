package com.GreenCat.common.FunctionManager;

import com.GreenCat.common.storage.SelectGUIStorage;
import com.GreenCat.common.ui.SelectGUI;
import com.GreenCat.type.SelectTable;


public class SelectGuiFunctionExecutant {
    Boolean FunctionStatus = true;
    public void RunFunction(String FunctionName){
        if(FunctionStatus) {
            FunctionManager.switchStatus(FunctionName);
        }
    }
    public void EnterTable(String TableName){
        SelectGUI.PresentGUI = TableName;
        for (SelectTable table : SelectGUIStorage.TableStorage) {
            if (table.getID().equals(SelectGUI.PresentGUI)) {
                SelectGUI.PresentFunction = table.getList().get(0).getName();
            }
        }
    }
    public void SetRunFunctionStatus(Boolean status){
        FunctionStatus = status;
    }
    public Boolean getRunFunctionStatus(){
        return FunctionStatus;
    }
}
