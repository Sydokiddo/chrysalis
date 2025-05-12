package net.sydokiddo.chrysalis.util.helpers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RegistryHelper {

    // region Registries

    // region Potion Recipes

    /**
     * Registry methods for registering potion recipes.
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

    // region Game Rules

    /**
     * Registry methods for custom game rules.
     **/

    public static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
    }

    public static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        return GameRules.register(name, category, GameRules.IntegerValue.create(defaultValue));
    }

    // endregion

    // region Data Packs

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

    public static final Codec<Holder<Block>> SINGULAR_BLOCK_CODEC = RegistryFixedCodec.create(Registries.BLOCK);

    // endregion

    // region Music

    /**
     * Registry methods for custom structure-specific music.
     **/

    public static void registerStructureMusic(String structureName, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        CRegistry.registeredStructures.put(structureName, new CRegistry.StructureMusicSound(soundEvent, minDelay, maxDelay, replaceCurrentMusic));
    }

    // endregion

    // region Particles

    /**
     * Registry methods for custom particles.
     **/

    public static SimpleParticleType registerSimpleParticle(boolean alwaysShow) {
        return new SimpleParticleType(alwaysShow);
    }

    public static <T extends ParticleOptions> ParticleType<T> registerAdvancedParticle(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean alwaysShow) {
        return new ParticleType<>(alwaysShow) {

            @Override
            public @NotNull MapCodec<T> codec() {
                return codec;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }

    // endregion

    // region Fluids

    /**
     * Registry methods for linking a fluidlogging state to a specific fluid type.
     **/

    public static void registerFluidForFluidlogging(Fluid fluid, FluidloggedState fluidloggedStateState) {
        if (Chrysalis.registryAccess == null) return;
        SimpleFluidloggedBlock.stateFromFluidMap.put(fluid, fluidloggedStateState);
    }

    // endregion

    // endregion

    // region Properties

    // region Items

    /**
     * Registry methods for registering various common item properties.
     **/

    public static Item.Properties iconProperties() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC);
    }

    public static Item.Properties debugUtilityProperties(int maxStackSize) {
        return new Item.Properties().stacksTo(maxStackSize).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    public static Item.Properties musicDiscProperties(ResourceKey<JukeboxSong> jukeboxSong, Rarity rarity) {
        return new Item.Properties().stacksTo(1).jukeboxPlayable(jukeboxSong).rarity(rarity);
    }

    public static Item.Properties mobContainerProperties(Item returnItem, Rarity rarity) {
        return new Item.Properties().stacksTo(1).craftRemainder(returnItem).rarity(rarity).rarity(rarity);
    }

    // endregion

    // region Blocks

    /**
     * Registry methods for registering various common block properties.
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

    // endregion
}