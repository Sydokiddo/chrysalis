package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface CustomCrosshairItem {

    /**
     * Any item that integrates this interface can be given a custom crosshair that can be displayed under certain conditions.
     **/

    default boolean shouldDisplayCrosshair(Player player) {
        return true;
    }

    default ResourceLocation getCrosshairTextureLocation() {
        return null;
    }
}