package com.greencat.antimony.core.FunctionManager;

import com.greencat.Antimony;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.ui.ClassicSelectGUI;
import com.greencat.antimony.core.type.SelectTable;


public class SelectGuiFunctionExecutant {
    Boolean FunctionStatus = true;
    public void RunFunction(String FunctionName){
        if(FunctionStatus) {
            FunctionManager.switchStatus(FunctionName);
        }
    }
    public void EnterTable(String TableName){
        Antimony.PresentGUI = TableName;
        for (SelectTable table : SelectGUIStorage.TableStorage) {
            if (table.getID().equals(Antimony.PresentGUI)) {
                Antimony.PresentFunction = table.getList().get(0).getName();
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
