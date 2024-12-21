package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface CustomCrosshairItem {

    @Environment(EnvType.CLIENT)
    default boolean shouldDisplayCrosshair(Player player) {
        return true;
    }

    @Environment(EnvType.CLIENT)
    default ResourceLocation getCrosshairTextureLocation() {
        return null;
    }
}