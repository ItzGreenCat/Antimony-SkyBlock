package com.greencat.antimony.core.via.loader;

import com.greencat.Antimony;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;

import java.io.File;
import java.util.logging.Logger;

public class BackwardsLoader implements ViaBackwardsPlatform {
    private final File file;

    public BackwardsLoader(final File file) {
        this.init(this.file = new File(file, "ViaBackwards"));
    }

    @Override
    public Logger getLogger() {
        return Antimony.getjLogger();
    }

    @Override
    public void disable() {
    }

    @Override
    public boolean isOutdated() {
        return false;
    }

    @Override
    public File getDataFolder() {
        return new File(this.file, "config.yml");
    }
}
