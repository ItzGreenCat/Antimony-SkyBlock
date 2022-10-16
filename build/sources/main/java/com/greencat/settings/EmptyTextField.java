package com.greencat.settings;

public class EmptyTextField extends AbstractSettingOptionTextField{
    @Override
    public void setValue() {
    }

    @Override
    public void init() {
        this.setTextFieldID(Integer.MAX_VALUE);
        this.setFocused(false);
        this.setCanLoseFocus(false);
        this.name = "Empty";
        this.ID = "Empty";
        this.parentFunction = "null";
    }

    @Override
    public void update() {
    }
    @Override
    public Object getValue() {
        return 0;
    }
}
