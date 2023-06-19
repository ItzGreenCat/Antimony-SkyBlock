package com.greencat.antimony.core.register;

import com.greencat.Antimony;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.FontManager;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntimonyRegister {
    static long lastReload = 0;
    //All registered functions are stored
    public static Map<String,AntimonyFunction> FunctionList = new HashMap<String,AntimonyFunction>();
    public static CopyOnWriteArrayList<AntimonyFunction> enabledList = new CopyOnWriteArrayList<AntimonyFunction>();
    //GUI list storage
    SelectGUIStorage SelectGuiStorage = new SelectGUIStorage();
    //Register a function
    public void RegisterFunction(AntimonyFunction function) {
        FunctionList.put(function.getName(),function);
        Antimony.LOGGER.info("Register Function -> " + function.getName());
    }
    //Reorder the list of functions to make the FunctionList more beautiful
    public static void ReList(){
        Collection<AntimonyFunction> originalList = FunctionList.values();
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
        for(AntimonyFunction function : newList){
            FunctionList.put(function.getName(),function);
        }
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
        Antimony.LOGGER.info("Register Select Object -> " + object.getName());
    }
    //Register a empty list,you still need to invoke RegisterSelectObject to add SelectObject into list
    public void RegisterTable(SelectTable table) {
        SelectGUIStorage.TableStorage.add(table);
    }
    public static void reloadEnableList(){
            if (Minecraft.getMinecraft().theWorld != null) {
                enabledList.clear();
                for (Map.Entry<String, AntimonyFunction> entry : FunctionList.entrySet()) {
                    if (entry.getValue().getStatus()) {
                        enabledList.add(entry.getValue());
                    }
                }
                Collections.sort(enabledList);
            }
    }
    public static void reloadEnableListUncheck(){
            if (System.currentTimeMillis() - lastReload > 1000) {
                lastReload = System.currentTimeMillis();
                enabledList.clear();
                for (Map.Entry<String, AntimonyFunction> entry : FunctionList.entrySet()) {
                    if (entry.getValue().getStatus()) {
                        enabledList.add(entry.getValue());
                    }
                }
                Collections.sort(enabledList);
            }
    }

}
