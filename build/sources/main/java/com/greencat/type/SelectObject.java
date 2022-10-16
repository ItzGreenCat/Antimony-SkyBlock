package com.greencat.type;


public class SelectObject {
    String SelectType;
    String FunctionName;
    String ParentID;

    public SelectObject(String type,String name,String parent){
        FunctionName = name;
        ParentID = parent;
        if(type.equals("function")){
            SelectType = "function";
        } else if (type.equals("table")) {
            SelectType = "table";
        }

    }
    public String getName(){
        return FunctionName;
    }
    public String getType(){
        return SelectType;
    }
    public String getParentID(){
        return ParentID;
    }
}
