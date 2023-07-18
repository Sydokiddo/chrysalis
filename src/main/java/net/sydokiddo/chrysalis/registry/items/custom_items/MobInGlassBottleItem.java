package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@SuppressWarnings("ALL")
public class MobInGlassBottleItem extends MobInContainerItem {

    public MobInGlassBottleItem(EntityType<?> entityType, SoundEvent soundEvent, Properties properties) {
        super(entityType, soundEvent, properties);
    }

    @Override
    public ItemStack getUsedItemStack() {
        return new ItemStack(Items.GLASS_BOTTLE);
    }
}
