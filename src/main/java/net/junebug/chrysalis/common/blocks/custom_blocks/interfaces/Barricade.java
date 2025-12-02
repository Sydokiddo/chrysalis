package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.junebug.chrysalis.client.particles.options.ColoredPortalParticleOptions;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.entities.custom_entities.block_entities.BarricadeBlockEntity;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface Barricade extends AmbientSoundEmittingBlock {

    /**
     * The interface used by barricades.
     **/

    static BlockBehaviour.Properties defaultProperties() {
        return properties(55.0F, 1200.0F, MapColor.COLOR_LIGHT_BLUE);
    }

    static BlockBehaviour.Properties unbreakableProperties() {
        return properties(-1.0F, 3600000.0F, MapColor.COLOR_RED);
    }

    private static BlockBehaviour.Properties properties(float destroyTime, float explosionResistance, MapColor mapColor) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).mapColor(mapColor).noLootTable().noOcclusion().isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK);
    }

    static void emitAmbientSoundsAndParticles(Level level, BlockPos blockPos, BlockState blockState, RandomSource randomSource, int particleColor, double particleOffset) {

        AmbientSoundEmittingBlock.playAmbientSound(level, blockPos, randomSource, CSoundEvents.BARRICADE_AMBIENT.get(), blockState.getValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS), 6000);
        if (!blockState.getValue(CBlockStateProperties.EMITS_PARTICLES) || randomSource.nextInt(20) != 0) return;

        for (int particleAmount = 0; particleAmount < 3; ++particleAmount) {

            double random1 = randomSource.nextInt(2) * 2.0D - 1.0D;
            double random2 = randomSource.nextInt(2) * 2.0D - 1.0D;

            double x = blockPos.getX() + particleOffset + 0.25D * random1;
            double y = blockPos.getY() + randomSource.nextFloat();
            double z = blockPos.getZ() + particleOffset + 0.25D * random2;

            double xSpeed = randomSource.nextFloat() * random1;
            double ySpeed = (randomSource.nextFloat() - 0.5D) * 0.15D;
            double zSpeed = randomSource.nextFloat() * random2;

            level.addParticle(new ColoredPortalParticleOptions(particleColor, particleColor, true, false), x, y, z, xSpeed, ySpeed, zSpeed);
        }
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

    static InteractionResult toggleVisibility(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, double particleRadius, int particleAmount, InteractionResult defaultInteraction) {

        if (player.canUseGameMasterBlocks() && player.isCrouching()) {

            SoundEvent soundEvent;
            ParticleOptions particleOptions;

            if (blockState.getValue(CBlockStateProperties.INVISIBLE)) {
                soundEvent = CSoundEvents.BARRICADE_MAKE_VISIBLE.get();
                particleOptions = ParticleTypes.HAPPY_VILLAGER;
            } else {
                soundEvent = CSoundEvents.BARRICADE_MAKE_INVISIBLE.get();
                particleOptions = ParticleTypes.SMOKE;
            }

            level.setBlockAndUpdate(blockPos, blockState.cycle(CBlockStateProperties.INVISIBLE));
            BarricadeBlockEntity.emitSoundAndParticles(level, player, blockPos, soundEvent, false, GameEvent.BLOCK_CHANGE, particleOptions, particleRadius, particleAmount);
            return InteractionResult.SUCCESS;
        }

        return defaultInteraction;
    }
}