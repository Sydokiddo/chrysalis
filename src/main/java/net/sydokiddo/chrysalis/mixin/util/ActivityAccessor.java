package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("ALL")
@Mixin(Activity.class)
public interface ActivityAccessor {

    /**
     * Accesses the activity class for mobs.
     **/

    @Invoker("<init>")
    static Activity createActivity(String string) {
        throw new UnsupportedOperationException();
    }
}