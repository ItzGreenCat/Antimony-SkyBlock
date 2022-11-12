package com.greencat.antimony.core.exceptions;

public class NoSuchConfigurationException extends Exception{
    public NoSuchConfigurationException(String id) {
        super("无法找到id为 " + id + " 的配置项目");
    }
}
