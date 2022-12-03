package com.greencat.antimony.core.type;

import com.greencat.antimony.core.exceptions.NoSuchConfigurationException;
import com.greencat.antimony.core.settings.AbstractSettingOptionButton;
import com.greencat.antimony.core.settings.AbstractSettingOptionTextField;
import com.greencat.antimony.core.settings.ISettingOption;

import java.util.ArrayList;
import java.util.List;

public class AntimonyFunction {
    String Name;
    Boolean Status = false;
    Boolean Configurable = false;
    List<ISettingOption> configurationOptions = new ArrayList<>();

    public AntimonyFunction(String FunctionName){
        Name = FunctionName;
    }
    @Deprecated
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
        Status = !Status;
    }
    public AntimonyFunction addConfigurationOption(ISettingOption option){
        if(!Configurable){
           Configurable = true;
        }
        configurationOptions.add(option);
        return this;
    }
    public Object getConfigurationValue(String ID) throws NoSuchConfigurationException {
        for(ISettingOption option : configurationOptions){
            if(option instanceof AbstractSettingOptionButton) {
                if(((AbstractSettingOptionButton) option).ID.equals(ID)) {
                    return option.getValue();
                }
            }
            if(option instanceof AbstractSettingOptionTextField) {
                if(((AbstractSettingOptionTextField) option).ID.equals(ID)) {
                    return option.getValue();
                }
            }
        }
        throw new NoSuchConfigurationException(ID);
    }
    public List<ISettingOption> getConfigurationList(){
        return configurationOptions;
    }
    public Boolean isConfigurable(){
        return Configurable;
    }
}
