package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.world.level.block.SoundType;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.DissipatingMultifaceBlock;

public class ExampleDissipatingMultifaceBlock extends DissipatingMultifaceBlock {

    /**
     * An example class to show how to set up a dissipating multiface block.
     **/

    public ExampleDissipatingMultifaceBlock(Properties properties) {
        super(properties, true, 600, false, SoundType.DECORATED_POT, SoundType.DECORATED_POT_CRACKED);
    }
}