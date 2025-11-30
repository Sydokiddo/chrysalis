package net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.Random;
import java.util.function.IntFunction;

public enum KeyGolemVariant implements StringRepresentable {

    GOLDEN(0, "golden", Color.decode("#FFAA00").getRGB()),
    DIAMOND(1, "diamond", Color.decode("#00BFFF").getRGB());

    private static final IntFunction<KeyGolemVariant> BY_ID = ByIdMap.sparse(KeyGolemVariant::id, KeyGolemVariant.values(), GOLDEN);

    private final int id;
    private final String name;
    private final int particleColor;

    KeyGolemVariant(int id, String name, int particleColor) {
        this.id = id;
        this.name = name;
        this.particleColor = particleColor;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }

    public static KeyGolemVariant byId(int id) {
        return BY_ID.apply(id);
    }

    public static KeyGolemVariant byRandomId() {
        return byId(new Random().nextInt(0, values().length));
    }

    public int particleColor() {
        return this.particleColor;
    }
}