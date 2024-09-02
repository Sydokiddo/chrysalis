package net.sydokiddo.chrysalis.misc.util.helpers;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.mixin.content.registry.BrewingRecipeRegistryBuilderMixin;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.registry.items.custom_items.CSpawnEggItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInFluidBucketItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInSolidBucketItem;

@SuppressWarnings("all")
public class RegistryHelper {

    // region Potion Recipe Registry

    /**
     * Registry helpers for registering potion recipes.
     **/

    public static void registerBasePotionRecipe(Item ingredient, Potion resultPotion) {
        BrewingRecipeRegistryBuilderMixin.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, ingredient, Holder.direct(resultPotion));
        });
    }

    public static void registerLongPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryBuilderMixin.BUILD.register(builder -> {
            builder.addMix(Holder.direct(startingPotion),  Items.REDSTONE, Holder.direct(resultPotion));
        });
    }

    public static void registerStrongPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryBuilderMixin.BUILD.register(builder -> {
            builder.addMix(Holder.direct(startingPotion),  Items.GLOWSTONE_DUST, Holder.direct(resultPotion));
        });
    }

    public static void registerInvertedPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryBuilderMixin.BUILD.register(builder -> {
            builder.addMix(Holder.direct(startingPotion),  Items.FERMENTED_SPIDER_EYE, Holder.direct(resultPotion));
        });
    }

    public static void registerUniquePotionRecipe(Potion startingPotion, Item ingredient, Potion resultPotion) {
        BrewingRecipeRegistryBuilderMixin.BUILD.register(builder -> {
            builder.addMix(Holder.direct(startingPotion),  ingredient, Holder.direct(resultPotion));
        });
    }

    // endregion

    // region Item Registry

    /**
     * Registry helpers for registering items with specific properties.
     **/

    public static SwordItem registerSword(Tier tier, float attackDamage, float attackSpeed) {
        return new SwordItem(tier, new Item.Properties().attributes(SwordItem.createAttributes(tier, (int) attackDamage, attackSpeed)));
    }

    public static PickaxeItem registerPickaxe(Tier tier, float attackDamage, float attackSpeed) {
        return new PickaxeItem(tier, new Item.Properties().attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    public static AxeItem registerAxe(Tier tier, float attackDamage, float attackSpeed) {
        return new AxeItem(tier, new Item.Properties().attributes(AxeItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    public static ShovelItem registerShovel(Tier tier, float attackDamage, float attackSpeed) {
        return new ShovelItem(tier, new Item.Properties().attributes(ShovelItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    public static HoeItem registerHoe(Tier tier, float attackDamage, float attackSpeed) {
        return new HoeItem(tier, new Item.Properties().attributes(HoeItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    public static ArmorItem registerHelmet(ArmorMaterial armorMaterial, int durability) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(durability)));
    }

    public static ArmorItem registerChestplate(ArmorMaterial armorMaterial, int durability) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(durability)));
    }

    public static ArmorItem registerLeggings(ArmorMaterial armorMaterial, int durability) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(durability)));
    }

    public static ArmorItem registerBoots(ArmorMaterial armorMaterial, int durability) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(durability)));
    }

    public static Item registerMusicDisc(Rarity rarity, ResourceKey<JukeboxSong> jukeboxSong) {
        return new Item(new Item.Properties().stacksTo(1).rarity(rarity).jukeboxPlayable(jukeboxSong));
    }

    public static CSpawnEggItem registerSpawnEgg(EntityType entityType, int baseColor, int spotsColor, EntityType mobOffspring) {
       return new CSpawnEggItem(entityType, baseColor, spotsColor, mobOffspring, new Item.Properties());
    }

    public static MobInContainerItem registerMobInContainer(EntityType entityType, SoundEvent soundEvent, Item returnItem) {
        return new MobInContainerItem(entityType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(returnItem), returnItem);
    }

    public static MobInFluidBucketItem registerMobInFluidBucket(EntityType entityType, Fluid fluidType, SoundEvent soundEvent) {
        return new MobInFluidBucketItem(entityType, fluidType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static MobInSolidBucketItem registerMobInSolidBucket(EntityType entityType, Block blockType, SoundEvent soundEvent) {
        return new MobInSolidBucketItem(entityType, blockType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET), Items.BUCKET);
    }

    // endregion

    // region Block Registry

    /**
     * Registry helpers for registering blocks with specific properties.
     **/

    public static ButtonBlock registerButton(BlockSetType blockSetType, int ticksToStayPressed) {
        return new ButtonBlock(blockSetType, ticksToStayPressed, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    public static PressurePlateBlock registerStonePressurePlate(BlockSetType blockSetType, MapColor mapColor) {
        return new PressurePlateBlock(blockSetType, BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn()
        .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    public static PressurePlateBlock registerWoodenPressurePlate(BlockSetType blockSetType, MapColor mapColor) {
        return new PressurePlateBlock(blockSetType, BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn()
        .instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    public static LeavesBlock registerLeaves(SoundType soundType, MapColor mapColor) {
        return new LeavesBlock(BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.2F).randomTicks()
        .sound(soundType).noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never)
        .isViewBlocking(Blocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never));
    }

    public static RotatedPillarBlock registerLog(MapColor innerColor, MapColor sideColor, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? innerColor : sideColor)
        .instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(soundType));
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
        if (Sets.newHashSet().add(resourceLocation)) return resourceLocation;
        throw new IllegalArgumentException(resourceLocation + " is already a registered built-in loot table");
    }

    // endregion
}