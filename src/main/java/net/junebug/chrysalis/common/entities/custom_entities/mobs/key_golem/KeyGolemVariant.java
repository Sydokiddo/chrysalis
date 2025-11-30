package net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.Random;

public enum KeyGolemVariant implements StringRepresentable {

    GOLDEN(0, "golden", false, Color.decode("#FFAA00").getRGB()),
    DIAMOND(1, "diamond", false, Color.decode("#00BFFF").getRGB()),
    ENCHANTED(2, "enchanted", true, Color.decode("#D699FF").getRGB());

    private final int
        id,
        particleColor
    ;

    private final String name;
    private final boolean isEnchanted;

    KeyGolemVariant(int id, String name, boolean isEnchanted, int particleColor) {
        this.id = id;
        this.name = name;
        this.isEnchanted = isEnchanted;
        this.particleColor = particleColor;
    }

    public int id() {
        return this.id;
    }

    public static KeyGolemVariant byId(int id) {
        return ByIdMap.sparse(KeyGolemVariant::id, KeyGolemVariant.values(), GOLDEN).apply(id);
    }

    @SuppressWarnings("unused")
    public static KeyGolemVariant byRandomId() {
        return byId(new Random().nextInt(0, values().length));
    }

    public static KeyGolemVariant byRandomIdWithoutEnchanted() {
        return byId(new Random().nextInt(0, values().length - 1));
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean isEnchanted() {
        return this.isEnchanted;
    }

    public int particleColor() {
        return this.particleColor;
    }
}