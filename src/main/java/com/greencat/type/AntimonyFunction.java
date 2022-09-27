package com.greencat.type;

public class AntimonyFunction {
    String Name;
    Boolean Status = false;

    public AntimonyFunction(String FunctionName){
        Name = FunctionName;
    }
    public int color;
    public String getName(){
        return Name;
    }
    public void setStatus(Boolean FunctionStatus){
        Status = FunctionStatus;
    }
    public Boolean getStatus(){
        return Status;
    }
    public void SwtichStatus(){
        if(Status){
            Status = false;
        } else {
            Status = true;
        }
    }
}
