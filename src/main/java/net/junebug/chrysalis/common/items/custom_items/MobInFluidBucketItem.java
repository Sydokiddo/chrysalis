package net.junebug.chrysalis.common.items.custom_items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

public class MobInFluidBucketItem extends MobBucketItem {

    /**
     * The base class for any mob container item that places a fluid alongside it.
     **/

    public MobInFluidBucketItem(EntityType<? extends Mob> entityType, Fluid fluidType, SoundEvent emptySound, Properties properties) {
        super(entityType, fluidType, emptySound, properties);
    }
}