package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.DissipatingLayerBlock;

public class ExampleDissipatingLayerBlock extends DissipatingLayerBlock {

    /**
     * An example class to show how to set up a dissipating layer block.
     **/

    public ExampleDissipatingLayerBlock(Properties properties) {
        super(properties, BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON, BlockTags.SNOW_LAYER_CAN_SURVIVE_ON, true, 600, false, SoundType.DECORATED_POT, SoundType.DECORATED_POT_CRACKED);
    }
}