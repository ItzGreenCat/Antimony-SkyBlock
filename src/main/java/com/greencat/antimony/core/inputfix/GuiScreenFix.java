package com.greencat.antimony.core.inputfix;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;

public class GuiScreenFix {
    public static IGuiScreenFix impl = new GuiScreenFixWindows();
    private static class Proxy implements IGuiScreen
    {

        private GuiScreen gui;

        @Override
        public void keyTyped(char c, int k)
        {
            try
            {
                if (gui != null)
                    keyTyped.invoke(gui, c, k);
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        }

        public Proxy setGui(GuiScreen gui)
        {
            this.gui = gui;
            return this;
        }

    }

    private static final ThreadLocal<Proxy> proxies = new ThreadLocal<Proxy>()
    {

        @Override
        protected Proxy initialValue()
        {
            return new Proxy();
        }

    };

    private static final Method keyTyped = findMethod(GuiScreen.class, new String[] { "func_73869_a", "keyTyped" }, new Class<?>[] { char.class, int.class });

    public static void handleKeyboardInput(GuiScreen gui)
    {
        Proxy p = proxies.get().setGui(gui);

        if (impl != null)
            impl.handleKeyboardInput(p);
        else if (Keyboard.getEventKeyState())
            p.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());

        gui.mc.dispatchKeypresses();
    }
    public static Method findMethod(Class<?> clazz, String[] methodNames, Class<?>[] methodTypes)
    {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new RuntimeException(failed);
    }
    public static class GuiScreenFixWindows implements IGuiScreenFix
    {

        @Override
        public void handleKeyboardInput(IGuiScreen gui)
        {
            char c = Keyboard.getEventCharacter();
            int k = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState() || (k == 0 && Character.isDefined(c)))
            {
                gui.keyTyped(c, k);
            }
        }

    }
}
