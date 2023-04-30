package net.sydokiddo.chrysalis.misc.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

@SuppressWarnings("ALL")
public class ChrysalisASM implements Runnable {

    @Override
    public void run() {

        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        // Enchantment Targets

        String enchantmentTarget = remapper.mapClassName("intermediary", "net.minecraft.class_1886");

        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("ELYTRA", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.ElytraEnchantTarget").build();
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("SHIELD", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.ShieldEnchantTarget").build();
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("BRUSH", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.BrushEnchantTarget").build();
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("HORSE_ARMOR", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.HorseArmorEnchantTarget").build();
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("SPYGLASS", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.SpyglassEnchantTarget").build();
    }
}