package net.sydokiddo.chrysalis.registry;

import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.particles.options.*;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.util.blocks.dispensers.PlaceBlockDispenserBehavior;
import net.sydokiddo.chrysalis.util.blocks.dispensers.PlaceEntityDispenserBehavior;
import net.sydokiddo.chrysalis.util.blocks.dispensers.ShootProjectileDispenserBehavior;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.registry.blocks.custom_blocks.ExampleSeatBlock;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.items.custom_items.*;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;

@SuppressWarnings("unused")
public class ChrysalisExampleRegistry {

    /**
     * A variety of examples for things that can be registered with ChrysalisMod.
     * <p>
     * Note: Do not initialize any methods in this class unless you want a bunch of broken items, blocks, and other things in your game!
     **/

    public ChrysalisExampleRegistry() {
        ChrysalisMod.LOGGER.warn("WARNING: {} example registry has been initialized.", ChrysalisMod.LOGGER.getName());
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

    public static final Item EXAMPLE_ICON = ChrysalisDebugItems.registerItem("example_icon", IconItem::new, ChrysalisDebugItems.debugUtilitySettings());

    public static final Item EXAMPLE_TOOL = ChrysalisDebugItems.registerItem("example_tool",
        properties -> new SwordItem(ToolMaterial.IRON, 3, -2.4F, properties), new Item.Properties());

    public static final Item EXAMPLE_ARMOR = ChrysalisDebugItems.registerItem("example_armor",
        properties -> new ArmorItem(ArmorMaterials.IRON, ArmorType.CHESTPLATE, properties), new Item.Properties());

    public static final Item EXAMPLE_MUSIC_DISC = ChrysalisDebugItems.registerItem("example_music_disc",
        Item::new, RegistryHelper.musicDiscProperties(JukeboxSongs.CAT, Rarity.COMMON));

    public static final Item EXAMPLE_SPAWN_EGG = ChrysalisDebugItems.registerItem("example_spawn_egg",
        properties -> new CSpawnEggItem(EntityType.FROG, EntityType.TADPOLE, properties), new Item.Properties());

    public static final Item EXAMPLE_MOB_CUSTOM_CONTAINER = ChrysalisDebugItems.registerItem("example_mob_custom_container",
        properties -> new MobInContainerItem(EntityType.ALLAY, SoundEvents.BUCKET_EMPTY, properties), RegistryHelper.mobContainerProperties(Items.GLASS_BOTTLE, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_FLUID_BUCKET = ChrysalisDebugItems.registerItem("example_mob_fluid_bucket",
        properties -> new MobInFluidBucketItem(EntityType.COD, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    public static final Item EXAMPLE_MOB_SOLID_BUCKET = ChrysalisDebugItems.registerItem("example_mob_solid_bucket",
        properties -> new MobInSolidBucketItem(EntityType.SNOW_GOLEM, Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    public static final Item EXAMPLE_ARMOR_TRIM_SMITHING_TEMPLATE = ChrysalisDebugItems.registerItem("example_armor_trim_smithing_template",
        SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final Item EXAMPLE_UPGRADE_SMITHING_TEMPLATE = ChrysalisDebugItems.registerItem("example_upgrade_smithing_template",
        properties -> CSmithingTemplateItem.createUpgradeTemplate("minecraft", "netherite_upgrade", CSmithingTemplateItem.EMPTY_SLOT_INGOT, properties),
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

    public static final ResourceLocation EXAMPLE_GAMEPLAY_LOOT_TABLE = RegistryHelper.registerCustomLootTable(ChrysalisMod.id("gameplay/example_gameplay_loot_table"));

    public static void registerExampleStructureMusic() {
        RegistryHelper.registerStructureMusic("minecraft:village_plains", SoundEvents.MUSIC_CREDITS, 20, 600, false);
    }

    public static void registerExampleDispenserMethods() {
        DispenserBlock.registerBehavior(Blocks.SAND.asItem(), new PlaceBlockDispenserBehavior());
        DispenserBlock.registerBehavior(Items.END_CRYSTAL, new PlaceEntityDispenserBehavior(EntityType.END_CRYSTAL, new Vec3(1.0D, 2.0D, 1.0D), new Vec3(0.5D, 0.0D, 0.5D), ChrysalisTags.END_CRYSTAL_BASE_BLOCKS, SoundEvents.AMETHYST_BLOCK_PLACE));
        DispenserBlock.registerBehavior(Items.BLAZE_POWDER, new ShootProjectileDispenserBehavior(EntityType.SMALL_FIREBALL, SoundEvents.BLAZE_SHOOT));
    }

    public static void sendExampleCameraShake(Entity entity) {
        EventHelper.sendCameraShakeToNearbyPlayers(entity, null, 10.0D, 20, 5, 5);
    }

    public static void sendExampleStatusEffect(Entity entity) {
        EventHelper.sendStatusEffectToNearbyPlayers(entity, null, 10.0D, new MobEffectInstance(ChrysalisEffects.RADIANCE, 40));
    }

    public static final ColoredDustPlumeParticleOptions EXAMPLE_COLORED_DUST_PLUME_PARTICLES = new ColoredDustPlumeParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), true, true, 1.0F);
    public static final DustExplosionParticleOptions EXAMPLE_DUST_EXPLOSION_PARTICLES = new DustExplosionParticleOptions(ComponentHelper.FIRE_COLOR.getRGB(), ComponentHelper.SOUL_FIRE_COLOR.getRGB(), true, 1.0F);
    public static final RotatingDustParticleOptions EXAMPLE_ROTATING_DUST_PARTICLES = new RotatingDustParticleOptions(ComponentHelper.MEMORY_FIRE_COLOR.getRGB(), true, true, false, 1.0F);
    public static final SparkleParticleOptions EXAMPLE_SPARKLE_PARTICLES = new SparkleParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false);
    public static final SmallPulsationParticleOptions EXAMPLE_SMALL_PULSATION_PARTICLES = new SmallPulsationParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false, Direction.UP.get3DDataValue(), 10);
    public static final LargePulsationParticleOptions EXAMPLE_LARGE_PULSATION_PARTICLES = new LargePulsationParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false, Direction.UP.get3DDataValue(), 20);

    // endregion
}