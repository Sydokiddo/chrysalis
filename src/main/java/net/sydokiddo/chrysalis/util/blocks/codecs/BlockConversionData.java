package net.sydokiddo.chrysalis.util.blocks.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.common.misc.CGameEvents;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;

public record BlockConversionData(HolderSet<Block> startingBlocks, Holder<Block> resultingBlock, HolderSet<Item> usedItems, String useInteraction, Holder<Item> returnedItem, Holder<SoundEvent> soundEvent, boolean randomizeSoundPitch, Holder<GameEvent> gameEvent, Holder<Block> particleState, String sneakingRequirement, String enabled) {

    /**
     * Converts information from a json file into specified block conversion data.
     **/

    public static final Codec<BlockConversionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("starting_blocks").forGetter(BlockConversionData::startingBlocks),
        RegistryHelper.SINGULAR_BLOCK_CODEC.fieldOf("resulting_block").forGetter(BlockConversionData::resultingBlock),
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("used_items").forGetter(BlockConversionData::usedItems),
        Codec.STRING.optionalFieldOf("use_interaction", ComponentHelper.noneString).forGetter(BlockConversionData::useInteraction),
        Item.CODEC.optionalFieldOf("returned_item", Holder.direct(Items.AIR)).forGetter(BlockConversionData::returnedItem),
        SoundEvent.CODEC.optionalFieldOf("sound_event", Holder.direct(SoundEvents.EMPTY)).forGetter(BlockConversionData::soundEvent),
        Codec.BOOL.optionalFieldOf("randomize_sound_pitch", false).forGetter(BlockConversionData::randomizeSoundPitch),
        GameEvent.CODEC.optionalFieldOf("game_event", CGameEvents.EMPTY).forGetter(BlockConversionData::gameEvent),
        RegistryHelper.SINGULAR_BLOCK_CODEC.optionalFieldOf("particle_state", Blocks.AIR.defaultBlockState().getBlockHolder()).forGetter(BlockConversionData::particleState),
        Codec.STRING.optionalFieldOf("sneaking_requirement", ComponentHelper.noneString).forGetter(BlockConversionData::sneakingRequirement),
        Codec.STRING.optionalFieldOf(ComponentHelper.enabledString, ComponentHelper.trueString).forGetter(BlockConversionData::enabled)
    ).apply(instance, BlockConversionData::new));
}