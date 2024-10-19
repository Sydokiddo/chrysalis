package net.sydokiddo.chrysalis.misc.util.helpers;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.items.custom_items.CSpawnEggItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInFluidBucketItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInSolidBucketItem;

@SuppressWarnings("unused, unchecked, rawtypes")
public class RegistryHelper {

    // region Music Registry

    /**
     * Registry for custom structure-specific music.
     **/

    public static void registerStructureMusic(String structureName, Holder.Reference<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        ChrysalisRegistry.registeredStructures.put(structureName, new ChrysalisRegistry.StructureMusicSound(soundEvent.getRegisteredName(), minDelay, maxDelay, replaceCurrentMusic));
    }

    // endregion

    // region Potion Recipe Registry

    /**
     * Registry helpers for registering potion recipes.
     **/

    public static void registerBasePotionRecipe(Item ingredient, Holder<Potion> resultPotion) {
        registerPotionRecipe(Potions.AWKWARD, ingredient, resultPotion);
    }

    public static void registerLongPotionRecipe(Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(startingPotion, Items.REDSTONE, resultPotion);
    }

    public static void registerStrongPotionRecipe(Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(startingPotion, Items.GLOWSTONE_DUST, resultPotion);
    }

    public static void registerInvertedPotionRecipe(Holder<Potion> startingPotion, Holder<Potion> resultPotion) {
        registerPotionRecipe(startingPotion, Items.FERMENTED_SPIDER_EYE, resultPotion);
    }

    public static void registerPotionRecipe(Holder<Potion> startingPotion, Item ingredient, Holder<Potion> resultPotion) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.registerPotionRecipe(startingPotion, Ingredient.of(ingredient), resultPotion));
    }

    // endregion

    // region Item Registry

    /**
     * Registry helpers for registering items with specific properties.
     **/

    // region Tools

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

    // endregion

    // region Armor

    public static ArmorItem registerHelmet(Holder<ArmorMaterial> armorMaterial, int durability) {
        return registerArmor(armorMaterial, ArmorItem.Type.HELMET, ArmorItem.Type.HELMET.getDurability(durability));
    }

    public static ArmorItem registerChestplate(Holder<ArmorMaterial> armorMaterial, int durability) {
        return registerArmor(armorMaterial, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.CHESTPLATE.getDurability(durability));
    }

    public static ArmorItem registerLeggings(Holder<ArmorMaterial> armorMaterial, int durability) {
        return registerArmor(armorMaterial, ArmorItem.Type.LEGGINGS, ArmorItem.Type.LEGGINGS.getDurability(durability));
    }

    public static ArmorItem registerBoots(Holder<ArmorMaterial> armorMaterial, int durability) {
        return registerArmor(armorMaterial, ArmorItem.Type.BOOTS, ArmorItem.Type.BOOTS.getDurability(durability));
    }

    public static ArmorItem registerArmor(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type armorType, int durability) {
        return new ArmorItem(armorMaterial, armorType, new Item.Properties().durability(armorType.getDurability(durability)));
    }

    // endregion

    // region Miscellaneous

    public static Item registerMusicDisc(ResourceKey<JukeboxSong> jukeboxSong, Rarity rarity) {
        return new Item(new Item.Properties().stacksTo(1).jukeboxPlayable(jukeboxSong).rarity(rarity));
    }

    public static CSpawnEggItem registerSpawnEgg(EntityType entityType, int baseColor, int spotsColor, EntityType mobOffspring) {
       return new CSpawnEggItem(entityType, baseColor, spotsColor, mobOffspring, new Item.Properties());
    }

    public static MobInContainerItem registerMobInContainer(EntityType entityType, SoundEvent soundEvent, Item returnItem, Rarity rarity) {
        return new MobInContainerItem(entityType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(returnItem).rarity(rarity), returnItem);
    }

    public static MobInFluidBucketItem registerMobInFluidBucket(EntityType entityType, Fluid fluidType, SoundEvent soundEvent, Rarity rarity) {
        return new MobInFluidBucketItem(entityType, fluidType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).rarity(rarity));
    }

    public static MobInSolidBucketItem registerMobInSolidBucket(EntityType entityType, Block blockType, SoundEvent soundEvent, Rarity rarity) {
        return new MobInSolidBucketItem(entityType, blockType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).rarity(rarity), Items.BUCKET);
    }

    // endregion

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
        Sets.newHashSet().add(resourceLocation);
        return resourceLocation;
    }

    // endregion
}