package net.junebug.chrysalis.common.items.custom_items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface CustomCrosshairItem {

    /**
     * Any item that integrates this interface can be given a custom crosshair that can be displayed under certain conditions.
     **/

    @OnlyIn(Dist.CLIENT)
    default boolean shouldDisplayCrosshair(Player player) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    default ResourceLocation getCrosshairTextureLocation() {
        return null;
    }
}