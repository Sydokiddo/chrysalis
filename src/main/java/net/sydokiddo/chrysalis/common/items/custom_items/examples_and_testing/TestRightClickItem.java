package net.sydokiddo.chrysalis.common.items.custom_items.examples_and_testing;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.entities.custom_entities.entity_spawner.EntitySpawner;
import org.jetbrains.annotations.NotNull;

public class TestRightClickItem extends Item {

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
        EntitySpawner.create(level, player.getOnPos().getCenter());
    }
}