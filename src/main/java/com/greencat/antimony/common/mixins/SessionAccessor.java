package com.greencat.antimony.common.mixins;

import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface SessionAccessor {
    @Accessor("token")
    void setToken(String token);
    @Accessor("token")
    String getToken();
    @Accessor("playerID")
    void setID(String ID);
    @Accessor("playerID")
    String getID();
    @Accessor("username")
    void setName(String Name);
    @Accessor("username")
    String getName();
}
