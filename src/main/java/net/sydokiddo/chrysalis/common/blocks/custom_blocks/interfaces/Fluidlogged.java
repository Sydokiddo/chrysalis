package net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Fluidlogged implements StringRepresentable {

    AIR("air"),
    WATER("water"),
    LAVA("lava");

    private final String fluidName;

    Fluidlogged(String fluidName) {
        this.fluidName = fluidName;
    }

    public String toString() {
        return this.fluidName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.fluidName;
    }
}