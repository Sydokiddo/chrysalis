package net.junebug.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.world.level.block.SoundType;
import net.junebug.chrysalis.common.blocks.custom_blocks.DissipatingBlock;

public class ExampleDissipatingBlock extends DissipatingBlock {

    /**
     * An example class to show how to set up a dissipating block.
     **/

    public ExampleDissipatingBlock(Properties properties) {
        super(properties, true, 600, false, SoundType.DECORATED_POT, SoundType.DECORATED_POT_CRACKED);
    }
}