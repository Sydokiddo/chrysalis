package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.world.level.block.SoundType;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.DissipatingBlock;

public class ExampleDissipatingBlock extends DissipatingBlock {

    public ExampleDissipatingBlock(Properties properties) {
        super(properties, true, 600, false, SoundType.DECORATED_POT, SoundType.DECORATED_POT_CRACKED);
    }
}