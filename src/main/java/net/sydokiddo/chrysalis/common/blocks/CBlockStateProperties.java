package net.sydokiddo.chrysalis.common.blocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.Fluidlogged;

public class CBlockStateProperties {

    private static final String amountString = "amount";

    @SuppressWarnings("unused")
    public static final IntegerProperty
        AMOUNT_MAX_2 = IntegerProperty.create(amountString, 1, 2),
        AMOUNT_MAX_3 = IntegerProperty.create(amountString, 1, 3),
        AMOUNT_MAX_4 = IntegerProperty.create(amountString, 1, 4)
    ;

    public static final BooleanProperty
        SITTABLE = BooleanProperty.create("sittable"),
        DISSIPATES = BooleanProperty.create("dissipates"),
        DESTROYED = BooleanProperty.create("destroyed")
    ;

    public static final EnumProperty<Fluidlogged> FLUIDLOGGED = EnumProperty.create("fluidlogged", Fluidlogged.class);
}