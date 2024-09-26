package net.sydokiddo.chrysalis.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.dispenser.PlaceBlockDispenserBehavior;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.misc.util.helpers.RegistryHelper;

@SuppressWarnings("unused")
public class ChrysalisExampleRegistry {

    /**
     * A variety of examples for things that can be registered with Chrysalis.
     * Note: Do not initialize this class unless you want a bunch of broken items, blocks, and other things in your game!
     **/

    // region Example Potion Recipe Registries

    public static void registerExamplePotions() {
        RegistryHelper.registerBasePotionRecipe(Items.GHAST_TEAR, Potions.REGENERATION);
        RegistryHelper.registerLongPotionRecipe(Potions.REGENERATION, Potions.LONG_REGENERATION);
        RegistryHelper.registerStrongPotionRecipe(Potions.REGENERATION, Potions.STRONG_REGENERATION);
        RegistryHelper.registerInvertedPotionRecipe(Potions.REGENERATION, Potions.POISON);
    }

    // endregion

    // region Example Item Registries

    public static final Item EXAMPLE_TOOL = registerItem("example_tool",
        RegistryHelper.registerSword(Tiers.IRON, 3, -2.4F));

    public static final Item EXAMPLE_ARMOR = registerItem("example_armor",
        RegistryHelper.registerChestplate(ArmorMaterials.IRON.value(), 15));

    public static final Item EXAMPLE_MUSIC_DISC = registerItem("example_music_disc",
        RegistryHelper.registerMusicDisc(JukeboxSongs.CAT, Rarity.COMMON));

    public static final Item EXAMPLE_SPAWN_EGG = registerItem("example_spawn_egg",
        RegistryHelper.registerSpawnEgg(EntityType.FROG, 16777215, 13421772, EntityType.TADPOLE));

    public static final Item EXAMPLE_MOB_CUSTOM_CONTAINER = registerItem("example_mob_custom_container",
        RegistryHelper.registerMobInContainer(EntityType.ALLAY, SoundEvents.BUCKET_EMPTY, Items.GLASS_BOTTLE, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_FLUID_BUCKET = registerItem("example_mob_fluid_bucket",
        RegistryHelper.registerMobInFluidBucket(EntityType.COD, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_SOLID_BUCKET = registerItem("example_mob_solid_bucket",
        RegistryHelper.registerMobInSolidBucket(EntityType.SNOW_GOLEM, Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, Rarity.COMMON));

    // endregion

    // region Example Block Registries

    public static final Block EXAMPLE_BUTTON = registerBlock("example_button",
        RegistryHelper.registerButton(BlockSetType.STONE, 20));

    public static final Block EXAMPLE_STONE_PRESSURE_PLATE = registerBlock("example_stone_pressure_plate",
        RegistryHelper.registerStonePressurePlate(BlockSetType.STONE, MapColor.STONE));

    public static final Block EXAMPLE_WOODEN_PRESSURE_PLATE = registerBlock("example_wooden_pressure_plate",
        RegistryHelper.registerWoodenPressurePlate(BlockSetType.OAK, MapColor.WOOD));

    public static final Block EXAMPLE_LEAVES = registerBlock("example_leaves",
        RegistryHelper.registerLeaves(SoundType.AZALEA_LEAVES, MapColor.PLANT));

    public static final Block EXAMPLE_LOG = registerBlock("example_log",
        RegistryHelper.registerLog(MapColor.WOOD, MapColor.PODZOL, SoundType.WOOD));

    // endregion

    // region Miscellaneous Example Registries

    public static final ResourceLocation EXAMPLE_GAMEPLAY_LOOT_TABLE = RegistryHelper.registerCustomLootTable(Chrysalis.id("gameplay/example_gameplay_loot_table"));

    public static void registerExampleMusic() {
        RegistryHelper.registerStructureMusic("minecraft:mineshaft", SoundEvents.MUSIC_CREDITS, 20, 600, false);
    }

    public static void registerExampleDispenserMethods() {
        DispenserBlock.registerBehavior(Blocks.SAND.asItem(), new PlaceBlockDispenserBehavior());
    }

    public static void sendExampleCameraShake(Entity entity) {
        EventHelper.sendCameraShakeToNearbyPlayers(entity, null, 10.0F, 20, 5, 5);
    }

    // endregion

    // region Registries

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Chrysalis.id(name), item);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Chrysalis.id(name),
        new BlockItem(block, new Item.Properties()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, Chrysalis.id(name), block);
    }

    // endregion
}