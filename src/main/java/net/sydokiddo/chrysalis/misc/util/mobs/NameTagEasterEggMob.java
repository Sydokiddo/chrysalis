package net.sydokiddo.chrysalis.misc.util.mobs;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Mob;

@SuppressWarnings("unused")
public interface NameTagEasterEggMob {

    /**
     * Gets a custom nametag name from a mob.
     **/

    default boolean getCustomNameTagName(String name, Mob mob) {
        return (mob.hasCustomName() && name.equals(ChatFormatting.stripFormatting(mob.getName().getString())));
    }
}