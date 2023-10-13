package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class ChrysalisDamageSources {

    // Damage Sources

    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_ARMOR = register("mob_attack_bypasses_armor");
    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_ENCHANTMENTS = register("mob_attack_bypasses_enchantments");
    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_SHIELD = register("mob_attack_bypasses_shield");
    public static final ResourceKey<DamageType> MOB_ATTACK_NO_KNOCKBACK = register("mob_attack_no_knockback");

    // Registry

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Chrysalis.id(name));
    }

    public static void registerDamageSources() {}
}