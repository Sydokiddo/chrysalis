package net.junebug.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.tags.BlockTags;
import net.junebug.chrysalis.common.blocks.custom_blocks.LayerBlock;
import net.junebug.chrysalis.util.helpers.ComponentHelper;

public class ExampleLayerBlock extends LayerBlock {

    /**
     * An example class to show how to set up a layer block.
     **/

    public ExampleLayerBlock(Properties properties) {
        super(properties, BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON, BlockTags.SNOW_LAYER_CAN_SURVIVE_ON, 2, ComponentHelper.CHRYSALIS_COLOR.getRGB());
    }
}