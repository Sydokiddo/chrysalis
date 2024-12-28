package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisDamageSources {

    // region Damage Sources

    @SuppressWarnings("unused")
    public static final ResourceKey<DamageType>
        MOB_ATTACK_BYPASSES_ARMOR = register("mob_attack_bypasses_armor"),
        MOB_ATTACK_BYPASSES_ENCHANTMENTS = register("mob_attack_bypasses_enchantments"),
        MOB_ATTACK_BYPASSES_SHIELD = register("mob_attack_bypasses_shield"),
        MOB_ATTACK_NO_KNOCKBACK = register("mob_attack_no_knockback"),
        KILL_WAND = register("kill_wand")
    ;

    // endregion

    // region Registry

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Chrysalis.id(name));
    }

    public static void registerDamageSources() {}

    // endregion
}