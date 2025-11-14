package net.junebug.chrysalis.common.blocks;

import net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;

@SuppressWarnings("unused")
public class CBlockStateProperties {

    /**
     * The registry for block states added by chrysalis.
     **/

    private static final String amountString = "amount";

    public static final IntegerProperty
        AMOUNT_MAX_2 = IntegerProperty.create(amountString, 1, 2),
        AMOUNT_MAX_3 = IntegerProperty.create(amountString, 1, 3),
        AMOUNT_MAX_4 = IntegerProperty.create(amountString, 1, 4)
    ;

    public static final BooleanProperty
        SITTABLE = BooleanProperty.create("sittable"),
        DISSIPATES = BooleanProperty.create("dissipates"),
        DESTROYED = BooleanProperty.create("destroyed"),
        EMITS_AMBIENT_SOUNDS = BooleanProperty.create("emits_ambient_sounds")
    ;

    public static final EnumProperty<FluidloggedState> FLUIDLOGGED = EnumProperty.create("fluidlogged", FluidloggedState.class);
    public static final EnumProperty<PlaceholderBlock.PlaceholderBlockModelState> PLACEHOLDER_BLOCK_MODEL_STATE = EnumProperty.create("model", PlaceholderBlock.PlaceholderBlockModelState.class);
    public static final EnumProperty<PlaceholderBlock.PlaceholderUpdateWhenState> PLACEHOLDER_UPDATE_WHEN_STATE = EnumProperty.create("update_when", PlaceholderBlock.PlaceholderUpdateWhenState.class);
}