package com.greencat.utils;

import java.lang.reflect.Method;

public class EasyReflection {
    private final Method method;

    public EasyReflection(Class targetClass, String targetMethod, Class... args) {
        try {
            this.method = targetClass.getDeclaredMethod(targetMethod, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void invoke(Object Instance) {
        try {
            this.method.setAccessible(true);
            this.method.invoke(Instance, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Utils utils = new Utils();
            utils.print("Antimony的反射机制又双叒叕炸啦!");
        }

    }


}
