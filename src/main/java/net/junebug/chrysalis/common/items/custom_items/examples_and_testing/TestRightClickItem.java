package net.junebug.chrysalis.common.items.custom_items.examples_and_testing;

import net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake.Earthquake;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.junebug.chrysalis.common.items.custom_items.CreativeModeDescriptionItem;
import org.jetbrains.annotations.NotNull;

public class TestRightClickItem extends CreativeModeDescriptionItem {

    public TestRightClickItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        this.doRightClickFunctionality(level, player, interactionHand);
        return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
    }

    @SuppressWarnings("unused")
    private void doRightClickFunctionality(Level level, Player player, InteractionHand interactionHand) {
        Earthquake.create(level, player.getOnPos().getBottomCenter());
    }
}