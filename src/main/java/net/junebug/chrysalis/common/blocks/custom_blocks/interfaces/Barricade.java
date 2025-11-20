package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import java.util.List;

public interface Barricade extends AmbientSoundEmittingBlock {

    static BlockBehaviour.Properties defaultProperties() {
        return BlockBehaviour.Properties.of().strength(55.0F, 1200.0F).mapColor(MapColor.COLOR_LIGHT_BLUE).noLootTable().noOcclusion().isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK);
    }

    static BlockBehaviour.Properties unbreakableProperties() {
        return BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).mapColor(MapColor.COLOR_LIGHT_BLUE).noLootTable().noOcclusion().isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK);
    }

    static void playAmbientSound(Level level, BlockPos blockPos, BlockState blockState, RandomSource randomSource) {
        AmbientSoundEmittingBlock.playAmbientSound(level, blockPos, randomSource, CSoundEvents.BARRICADE_AMBIENT.get(), blockState.getValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS), 3000);
    }

    static void addTooltip(List<Component> list, TooltipFlag tooltipFlag, boolean addSecondaryTooltip) {

        if (!tooltipFlag.isCreative()) return;

        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("block.chrysalis.barricade.description").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));

        if (addSecondaryTooltip) {
            list.add(CommonComponents.EMPTY);
            list.add(Component.translatable("block.chrysalis.barricade.description_secondary").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }
}