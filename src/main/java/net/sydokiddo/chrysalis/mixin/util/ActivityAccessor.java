package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Activity.class)
public interface ActivityAccessor {

    /**
     * Accesses the activity class for mobs.
     **/

    @SuppressWarnings("ALL")
    @Invoker("<init>")
    static Activity createActivity(String string) {
        throw new UnsupportedOperationException();
    }
}