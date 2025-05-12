package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.CustomTntBlock;

public class ExampleTntBlock extends CustomTntBlock {

    /**
     * An example class to show how to set up a custom tnt block.
     **/

    public ExampleTntBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Entity getEntity(Level level, BlockPos blockPos, LivingEntity owner) {
        return new PrimedTnt(level, (double) blockPos.getX() + 0.5D, blockPos.getY() + 0.0D, (double) blockPos.getZ() + 0.5D, owner);
    }

    @Override
    public SoundEvent getSoundEvent() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    public void setExplodeData(Entity entity) {
        super.setWasExplodedData(entity);
        entity.setGlowingTag(true);
    }

    @Override
    public void setWasExplodedData(Entity entity) {

        super.setWasExplodedData(entity);
        this.setExplodeData(entity);

        if (entity instanceof PrimedTnt primedTnt) {
            int fuse = primedTnt.getFuse();
            primedTnt.setFuse((short) (entity.level().getRandom().nextInt(fuse / 4) + fuse / 8));
        }
    }
}