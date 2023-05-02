package net.sydokiddo.chrysalis.misc.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class ChrysalisASM implements Runnable {

    @Override
    public void run() {

        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        // Enchantment Targets

        String enchantmentTarget = remapper.mapClassName("intermediary", "net.minecraft.class_1886");

        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("ELYTRA", "net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets.ElytraEnchantTarget").build();
    }
}