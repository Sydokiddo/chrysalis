package net.sydokiddo.chrysalis.util.helpers;

import com.google.common.collect.Sets;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;

@SuppressWarnings("unused")
public class RegistryHelper {

    // region Data Pack Registry

    /**
     * Registry methods for various custom data types for data packs.
     **/

    public static <T> ResourceKey<Registry<T>> registerBlockDataType(String string) {
        return Chrysalis.key("block/" + string);
    }

    public static <T> ResourceKey<Registry<T>> registerItemDataType(String string) {
        return Chrysalis.key("item/" + string);
    }

    public static <T> ResourceKey<Registry<T>> registerEntityDataType(String string) {
        return Chrysalis.key("entity/" + string);
    }

    public static <T> ResourceKey<Registry<T>> registerMobVariantDataType(String string) {
        return registerEntityDataType("mob_variant/" + string);
    }

    // endregion

    // region Music Registry

    /**
     * Registry for custom structure-specific music.
     **/

    public static void registerStructureMusic(String structureName, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        CRegistry.registeredStructures.put(structureName, new CRegistry.StructureMusicSound(soundEvent, minDelay, maxDelay, replaceCurrentMusic));
    }

    // endregion

    // region Potion Recipe Registry

    /**
     * Registry helpers for registering potion recipes.
     **/

    public static void registerBasePotionRecipe(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> resultPotion) {
        registerPotionRecipe(builder, Potions.AWKWARD, ingredient, resultPotion);
    }

    public static void registerLongPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(builder, startingPotion, Items.REDSTONE, resultPotion);
    }

    public static void registerStrongPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(builder, startingPotion, Items.GLOWSTONE_DUST, resultPotion);
    }

    public static void registerInvertedPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(builder, startingPotion, Items.FERMENTED_SPIDER_EYE, resultPotion);
    }

    public static void registerPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> startingPotion, Item ingredient, Holder<Potion> resultPotion) {
        builder.addMix(startingPotion, ingredient, resultPotion);
    }

    // endregion

    // region Item Properties

    /**
     * Registry helpers for registering various common item properties.
     **/

    public static Item.Properties musicDiscProperties(ResourceKey<JukeboxSong> jukeboxSong, Rarity rarity) {
        return new Item.Properties().stacksTo(1).jukeboxPlayable(jukeboxSong).rarity(rarity);
    }

    public static Item.Properties mobContainerProperties(Item returnItem, Rarity rarity) {
        return new Item.Properties().stacksTo(1).craftRemainder(returnItem).rarity(rarity).rarity(rarity);
    }

    // endregion

    // region Block Properties

    /**
     * Registry helpers for registering various common block properties.
     **/

    public static BlockBehaviour.Properties stonePressurePlateProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties woodenPressurePlateProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
        .noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties leavesProperties(MapColor mapColor, SoundType soundType, BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.2F).randomTicks().sound(soundType)
        .noOcclusion().isValidSpawn(isValidSpawn).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).ignitedByLava()
        .pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never);
    }

    // endregion

    // region Loot Table Registry

    /**
     * Registry helpers for assisting with custom loot tables.
     **/

    public static ResourceLocation registerCustomLootTable(String name) {
        return registerCustomLootTable(ResourceLocation.parse(name));
    }

    public static ResourceLocation registerCustomLootTable(ResourceLocation resourceLocation) {
        Sets.newHashSet().add(resourceLocation);
        return resourceLocation;
    }

    // endregion
}