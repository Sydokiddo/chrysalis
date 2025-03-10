package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.sydokiddo.chrysalis.ChrysalisMod;

public class ChrysalisDamageTypes {

    // region Damage Types

    @SuppressWarnings("unused")
    public static final ResourceKey<DamageType>
        MOB_ATTACK_BYPASSES_ARMOR = registerDamageType("mob_attack_bypasses_armor"),
        MOB_ATTACK_BYPASSES_ENCHANTMENTS = registerDamageType("mob_attack_bypasses_enchantments"),
        MOB_ATTACK_BYPASSES_SHIELD = registerDamageType("mob_attack_bypasses_shield"),
        MOB_ATTACK_NO_KNOCKBACK = registerDamageType("mob_attack_no_knockback"),
        KILL_WAND = registerDamageType("kill_wand")
    ;

    // endregion

    // region Registry

    private static ResourceKey<DamageType> registerDamageType(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ChrysalisMod.id(name));
    }

    public static void register() {}

    // endregion
}