package net.sydokiddo.chrysalis.common.items.custom_items.examples_and_testing;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.entities.custom_entities.EntitySpawner;
import net.sydokiddo.chrysalis.common.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
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
        EntitySpawner.spawn(
            level,
            EntityType.ZOMBIE,
            player.getOnPos().getCenter(),
            SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM_BEGIN,
            SoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM,
            SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM,
            ComponentHelper.MEMORY_FIRE_COLOR.getRGB(),
            CommonColors.WHITE,
            ChrysalisParticles.MEMORY_FLAME.get()
        );
    }
}