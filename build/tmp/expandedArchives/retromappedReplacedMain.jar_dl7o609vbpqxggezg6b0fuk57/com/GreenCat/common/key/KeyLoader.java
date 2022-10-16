package com.GreenCat.common.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyLoader {
    public static KeyBinding SelectGuiUP;
    public static KeyBinding SelectGuiDown;
    public static KeyBinding SelectGuiEnter;
    public static KeyBinding SelectGuiBack;

    public static KeyBinding HugeScreenshot;

    public KeyLoader()
    {
        KeyLoader.SelectGuiUP = new KeyBinding("GCA SelectGui上移", Keyboard.KEY_UP, "GreenCatAddon");
        KeyLoader.SelectGuiDown = new KeyBinding("GCA SelectGui下移", Keyboard.KEY_DOWN, "GreenCatAddon");
        KeyLoader.SelectGuiEnter = new KeyBinding("GCA SelectGui选中", Keyboard.KEY_RIGHT, "GreenCatAddon");
        KeyLoader.SelectGuiBack = new KeyBinding("GCA SelectGui返回初级菜单", Keyboard.KEY_LEFT, "GreenCatAddon");

        KeyLoader.HugeScreenshot = new KeyBinding("大型截图", Keyboard.KEY_G, "GreenCatAddon");

        ClientRegistry.registerKeyBinding(KeyLoader.SelectGuiUP);
        ClientRegistry.registerKeyBinding(KeyLoader.SelectGuiDown);
        ClientRegistry.registerKeyBinding(KeyLoader.SelectGuiEnter);
        ClientRegistry.registerKeyBinding(KeyLoader.SelectGuiBack);

        ClientRegistry.registerKeyBinding(KeyLoader.HugeScreenshot);
    }
}
