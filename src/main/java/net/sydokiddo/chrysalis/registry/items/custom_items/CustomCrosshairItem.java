package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface CustomCrosshairItem {

    default boolean shouldDisplayCrosshair(Player player) {
        return true;
    }

    default ResourceLocation getCrosshairTextureLocation() {
        return null;
    }
}