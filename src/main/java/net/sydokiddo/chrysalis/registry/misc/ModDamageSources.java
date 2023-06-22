package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.sydokiddo.chrysalis.Chrysalis;

public class ModDamageSources {

    // List of Damage Sources:

    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_ARMOR = register("mob_attack_bypasses_armor");
    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_ENCHANTMENTS = register("mob_attack_bypasses_enchantments");
    public static final ResourceKey<DamageType> MOB_ATTACK_BYPASSES_SHIELD = register("mob_attack_bypasses_shield");

    // Registry for Damage Sources:

    @SuppressWarnings("ALL")
    private static void bootstrap(BootstapContext<DamageType> context) {
        context.register(MOB_ATTACK_BYPASSES_ARMOR, new DamageType("mob_attack_bypasses_armor", 0.1F));
        context.register(MOB_ATTACK_BYPASSES_ENCHANTMENTS, new DamageType("mob_attack_bypasses_enchantments", 0.1F));
        context.register(MOB_ATTACK_BYPASSES_SHIELD, new DamageType("mob_attack_bypasses_shield", 0.1F));
    }

    private static ResourceKey<DamageType> register(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Chrysalis.id(path));
    }

    public static void registerDamageSources() {}
}
