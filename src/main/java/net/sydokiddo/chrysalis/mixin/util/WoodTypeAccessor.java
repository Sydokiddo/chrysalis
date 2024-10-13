package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(WoodType.class)
public interface WoodTypeAccessor {

    /**
     * Accesses the registry for wood types.
     **/

    @Invoker("register")
    static WoodType chrysalis$invokeRegisterWoodType(WoodType woodType) {
        throw new UnsupportedOperationException();
    }
}