package net.sydokiddo.chrysalis.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.dispenser.PlaceBlockDispenserBehavior;
import net.sydokiddo.chrysalis.misc.util.dispenser.ShootProjectileDispenserBehavior;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.misc.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.registry.blocks.custom_blocks.ExampleSeatBlock;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.items.custom_items.*;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;

@SuppressWarnings("unused")
public class ChrysalisExampleRegistry {

    /**
     * A variety of examples for things that can be registered with Chrysalis.
     * Note: Do not initialize any methods in this class unless you want a bunch of broken items, blocks, and other things in your game!
     **/

    public ChrysalisExampleRegistry() {
        Chrysalis.LOGGER.warn("WARNING: {} example registry has been initialized.", Chrysalis.LOGGER.getName());
    }

    // region Example Potion Recipe Registries

    public static void registerExamplePotions() {
        RegistryHelper.registerBasePotionRecipe(Items.GHAST_TEAR, Potions.REGENERATION);
        RegistryHelper.registerLongPotionRecipe(Potions.REGENERATION, Potions.LONG_REGENERATION);
        RegistryHelper.registerStrongPotionRecipe(Potions.REGENERATION, Potions.STRONG_REGENERATION);
        RegistryHelper.registerInvertedPotionRecipe(Potions.REGENERATION, Potions.POISON);
    }

    // endregion

    // region Example Item Registries

    public static final Item EXAMPLE_TOOL = ChrysalisDebugItems.registerItem("example_tool",
        properties -> new SwordItem(ToolMaterial.IRON, 3, -2.4F, properties), new Item.Properties());

    public static final Item EXAMPLE_ARMOR = ChrysalisDebugItems.registerItem("example_armor",
        properties -> new ArmorItem(ArmorMaterials.IRON, ArmorType.CHESTPLATE, properties), new Item.Properties());

    public static final Item EXAMPLE_MUSIC_DISC = ChrysalisDebugItems.registerItem("example_music_disc",
        Item::new, RegistryHelper.musicDiscProperties(JukeboxSongs.CAT, Rarity.COMMON));

    public static final Item EXAMPLE_SPAWN_EGG = ChrysalisDebugItems.registerItem("example_spawn_egg",
        properties -> new CSpawnEggItem(EntityType.FROG, 16777215, 13421772, EntityType.TADPOLE, properties), new Item.Properties());

    public static final Item EXAMPLE_MOB_CUSTOM_CONTAINER = ChrysalisDebugItems.registerItem("example_mob_custom_container",
        properties -> new MobInContainerItem(EntityType.ALLAY, SoundEvents.BUCKET_EMPTY, properties), RegistryHelper.mobContainerProperties(Items.GLASS_BOTTLE, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_FLUID_BUCKET = ChrysalisDebugItems.registerItem("example_mob_fluid_bucket",
        properties -> new MobInFluidBucketItem(EntityType.COD, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_SOLID_BUCKET = ChrysalisDebugItems.registerItem("example_mob_solid_bucket",
        properties -> new MobInSolidBucketItem(EntityType.SNOW_GOLEM, Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    public static final Item EXAMPLE_ARMOR_TRIM_SMITHING_TEMPLATE = ChrysalisDebugItems.registerItem("example_armor_trim_smithing_template",
        SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final Item EXAMPLE_UPGRADE_SMITHING_TEMPLATE = ChrysalisDebugItems.registerItem("example_upgrade_smithing_template",
        properties -> ModSmithingTemplateItem.createUpgradeTemplate("minecraft", "netherite_upgrade", ModSmithingTemplateItem.EMPTY_SLOT_INGOT, properties),
        new Item.Properties().rarity(Rarity.UNCOMMON));

    // endregion

    // region Example Block Registries

    public static final Block EXAMPLE_BUTTON = ChrysalisDebugItems.registerBlock("example_button",
        properties -> new ButtonBlock(BlockSetType.STONE, 20, properties), Blocks.buttonProperties(), new Item.Properties());

    public static final Block EXAMPLE_STONE_PRESSURE_PLATE = ChrysalisDebugItems.registerBlock("example_stone_pressure_plate",
        properties -> new PressurePlateBlock(BlockSetType.STONE, properties), RegistryHelper.stonePressurePlateProperties(MapColor.STONE), new Item.Properties());

    public static final Block EXAMPLE_WOODEN_PRESSURE_PLATE = ChrysalisDebugItems.registerBlock("example_wooden_pressure_plate",
        properties -> new PressurePlateBlock(BlockSetType.OAK, properties), RegistryHelper.woodenPressurePlateProperties(Blocks.OAK_PLANKS.defaultMapColor()), new Item.Properties());

    public static final Block EXAMPLE_LEAVES = ChrysalisDebugItems.registerBlock("example_leaves",
        LeavesBlock::new, RegistryHelper.leavesProperties(MapColor.PLANT, SoundType.AZALEA_LEAVES, Blocks::ocelotOrParrot), new Item.Properties());

    public static final Block EXAMPLE_LOG = ChrysalisDebugItems.registerBlock("example_log",
        RotatedPillarBlock::new, Blocks.logProperties(MapColor.WOOD, MapColor.PODZOL, SoundType.WOOD), new Item.Properties());

    public static final Block EXAMPLE_SITTABLE_BLOCK = ChrysalisDebugItems.registerBlock("example_sittable_block",
        ExampleSeatBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS), new Item.Properties());

    // endregion

    // region Miscellaneous Example Registries

    public static final ResourceLocation EXAMPLE_GAMEPLAY_LOOT_TABLE = RegistryHelper.registerCustomLootTable(Chrysalis.id("gameplay/example_gameplay_loot_table"));

    public static void registerExampleMusic() {
        RegistryHelper.registerStructureMusic("minecraft:village_plains", SoundEvents.MUSIC_CREDITS, 20, 600, false);
    }

    public static void registerExampleDispenserMethods() {
        DispenserBlock.registerBehavior(Blocks.SAND.asItem(), new PlaceBlockDispenserBehavior());
        DispenserBlock.registerBehavior(Items.BLAZE_POWDER, new ShootProjectileDispenserBehavior(EntityType.SMALL_FIREBALL, SoundEvents.BLAZE_SHOOT));
    }

    public static void sendExampleCameraShake(Entity entity) {
        EventHelper.sendCameraShakeToNearbyPlayers(entity, null, 10.0F, 20, 5, 5);
    }

    public static void sendExampleStatusEffect(Entity entity) {
        EventHelper.sendStatusEffectToNearbyPlayers(entity, null, 10.0F, new MobEffectInstance(ChrysalisEffects.RADIANCE, 40));
    }

    // endregion
}