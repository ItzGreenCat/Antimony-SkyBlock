package com.greencat.antimony.common;

import jline.internal.Log;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@IFMLLoadingPlugin.Name("Antimony on Top")
public class AntimonyMixinLoader implements IFMLLoadingPlugin {

    public AntimonyMixinLoader() {
        try {
            MixinBootstrap.init();
            Mixins.addConfiguration("mixins.antimony.json");
            MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        } catch(Exception | Error e){
            Logger.getLogger("AntimonyMixinLoader").log(Level.OFF,"Mixin cannot load successfully,some function may not available now.");
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}