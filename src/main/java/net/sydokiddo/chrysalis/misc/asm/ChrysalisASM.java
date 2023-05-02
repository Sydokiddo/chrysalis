package net.sydokiddo.chrysalis.misc.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

public class ChrysalisASM implements Runnable {

    private String getIntermediaryClass(String path) {
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", path);
    }

    @Override
    public void run() {

        // Enchantment Targets

        ClassTinkerers.enumBuilder(getIntermediaryClass("net.minecraft.class_1886")).addEnumSubclass("ELYTRA", "net.sydokiddo.chrysalis.asm.ElytraEnchantmentTarget").build();
    }
}