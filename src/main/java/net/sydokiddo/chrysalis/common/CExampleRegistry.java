package net.sydokiddo.chrysalis.common;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.particles.options.*;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing.*;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawner;
import net.sydokiddo.chrysalis.common.items.custom_items.examples_and_testing.ExampleBowItem;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.blocks.dispensers.PlaceBlockDispenserBehavior;
import net.sydokiddo.chrysalis.util.blocks.dispensers.PlaceEntityDispenserBehavior;
import net.sydokiddo.chrysalis.util.blocks.dispensers.ShootProjectileDispenserBehavior;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.common.items.CItems;
import net.sydokiddo.chrysalis.common.items.custom_items.*;
import net.sydokiddo.chrysalis.common.status_effects.CStatusEffects;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CExampleRegistry {

    /**
     * A variety of examples for things that can be registered with Chrysalis.
     * <p>
     * Note: Do not initialize any methods in this class unless you want a bunch of broken items, blocks, and other things in your game!
     **/

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        registerExampleStructureMusic();
        registerExampleDispenserMethods();
        registerExampleFluidsForFluidLogging();
        Chrysalis.LOGGER.warn("WARNING: {} example registry has been initialized.", Chrysalis.LOGGER.getName());
    }

    // region Example Potion Recipe Registries

    private static void registerExamplePotions(PotionBrewing.Builder builder) {
        RegistryHelper.registerBasePotionRecipe(builder, Items.GHAST_TEAR, Potions.REGENERATION);
        RegistryHelper.registerLongPotionRecipe(builder, Potions.REGENERATION, Potions.LONG_REGENERATION);
        RegistryHelper.registerStrongPotionRecipe(builder, Potions.REGENERATION, Potions.STRONG_REGENERATION);
        RegistryHelper.registerInvertedPotionRecipe(builder, Potions.REGENERATION, Potions.POISON);
    }

    // endregion

    // region Example Item Registries

    private static final DeferredItem<Item> EXAMPLE_ICON = CItems.ITEMS.registerItem("example_icon", IconItem::new, RegistryHelper.iconProperties());

    private static final DeferredItem<Item> EXAMPLE_TOOL = CItems.ITEMS.registerItem("example_tool",
        properties -> new SwordItem(ToolMaterial.IRON, 3, -2.4F, properties), new Item.Properties());

    private static final DeferredItem<Item> EXAMPLE_ARMOR = CItems.ITEMS.registerItem("example_armor",
        properties -> new ArmorItem(ArmorMaterials.IRON, ArmorType.CHESTPLATE, properties), new Item.Properties());

    private static final DeferredItem<Item> EXAMPLE_BOW = CItems.ITEMS.registerItem("example_bow", ExampleBowItem::new, new Item.Properties());

    private static final DeferredItem<Item> EXAMPLE_MUSIC_DISC = CItems.ITEMS.registerItem("example_music_disc",
        Item::new, RegistryHelper.musicDiscProperties(JukeboxSongs.CAT, Rarity.COMMON));

    private static final DeferredItem<Item> EXAMPLE_SPAWN_EGG = CItems.ITEMS.registerItem("example_spawn_egg",
        properties -> new CustomSpawnEggItem(EntityType.FROG, EntityType.TADPOLE, properties), new Item.Properties());

    private static final DeferredItem<Item> EXAMPLE_MOB_CUSTOM_CONTAINER = CItems.ITEMS.registerItem("example_mob_custom_container",
        properties -> new MobInContainerItem(EntityType.ALLAY, SoundEvents.BUCKET_EMPTY, properties), RegistryHelper.mobContainerProperties(Items.GLASS_BOTTLE, Rarity.COMMON));

    private static final DeferredItem<Item> EXAMPLE_MOB_FLUID_BUCKET = CItems.ITEMS.registerItem("example_mob_fluid_bucket",
        properties -> new MobInFluidBucketItem(EntityType.COD, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    private static final DeferredItem<Item> EXAMPLE_MOB_SOLID_BUCKET = CItems.ITEMS.registerItem("example_mob_solid_bucket",
        properties -> new MobInSolidBucketItem(EntityType.SNOW_GOLEM, Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties), RegistryHelper.mobContainerProperties(Items.BUCKET, Rarity.COMMON));

    private static final DeferredItem<Item> EXAMPLE_ARMOR_TRIM_SMITHING_TEMPLATE = CItems.ITEMS.registerItem("example_armor_trim_smithing_template",
        SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));

    private static final DeferredItem<Item> EXAMPLE_UPGRADE_SMITHING_TEMPLATE = CItems.ITEMS.registerItem("example_upgrade_smithing_template",
        properties -> CustomSmithingTemplateItem.createUpgradeTemplate("minecraft", "netherite_upgrade", CustomSmithingTemplateItem.INGOT, properties),
        new Item.Properties().rarity(Rarity.UNCOMMON));

    // endregion

    // region Example Block Registries

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chrysalis.MOD_ID);

    private static final DeferredBlock<Block> EXAMPLE_BUTTON = registerBlock("example_button",
        properties -> new ButtonBlock(BlockSetType.STONE, 20, properties), Blocks.buttonProperties(), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_STONE_PRESSURE_PLATE = registerBlock("example_stone_pressure_plate",
        properties -> new PressurePlateBlock(BlockSetType.STONE, properties), RegistryHelper.stonePressurePlateProperties(MapColor.STONE), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_WOODEN_PRESSURE_PLATE = registerBlock("example_wooden_pressure_plate",
        properties -> new PressurePlateBlock(BlockSetType.OAK, properties), RegistryHelper.woodenPressurePlateProperties(Blocks.OAK_PLANKS.defaultMapColor()), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_LEAVES = registerBlock("example_leaves",
        LeavesBlock::new, RegistryHelper.leavesProperties(MapColor.PLANT, SoundType.AZALEA_LEAVES, Blocks::ocelotOrParrot), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_LOG = registerBlock("example_log",
        RotatedPillarBlock::new, Blocks.logProperties(MapColor.WOOD, MapColor.PODZOL, SoundType.WOOD), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_LAYER_BLOCK = registerBlock("example_layer_block",
        ExampleLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_SITTABLE_BLOCK = registerBlock("example_sittable_block",
        ExampleSeatBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_DISSIPATING_BLOCK = registerBlock("example_dissipating_block",
        ExampleDissipatingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_DISSIPATING_LAYER_BLOCK = registerBlock("example_dissipating_layer_block",
        ExampleDissipatingLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_DISSIPATING_MULTIFACE_BLOCK = registerBlock("example_dissipating_multiface_block",
        ExampleDissipatingMultifaceBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RESIN_CLUMP), new Item.Properties());

    private static final DeferredBlock<Block> EXAMPLE_FLUIDLOGGED_BLOCK = registerBlock("example_fluidlogged_block",
        ExampleFluidloggedBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE), new Item.Properties());

    private static DeferredBlock<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties settings) {
        return registerBlock(name, function, settings, new Item.Properties());
    }

    private static DeferredBlock<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, function, blockSettings);
        CItems.ITEMS.registerSimpleBlockItem(block, itemSettings);
        return block;
    }

    // endregion

    // region Miscellaneous Example Registries

    private static void registerExampleStructureMusic() {
        RegistryHelper.registerStructureMusic("minecraft:village_plains", SoundEvents.MUSIC_CREDITS, 20, 600, false);
    }

    private static void registerExampleDispenserMethods() {
        DispenserBlock.registerBehavior(Blocks.SAND.asItem(), new PlaceBlockDispenserBehavior());
        DispenserBlock.registerBehavior(Items.END_CRYSTAL, new PlaceEntityDispenserBehavior(EntityType.END_CRYSTAL, new Vec3(1.0D, 2.0D, 1.0D), new Vec3(0.5D, 0.0D, 0.5D), CTags.END_CRYSTAL_BASE_BLOCKS, SoundEvents.AMETHYST_BLOCK_PLACE));
        DispenserBlock.registerBehavior(Items.BLAZE_POWDER, new ShootProjectileDispenserBehavior(EntityType.SMALL_FIREBALL, SoundEvents.BLAZE_SHOOT));
    }

    private static void sendExampleCameraShake(Entity entity) {
        EventHelper.sendCameraShakeToNearbyPlayers(entity, null, 10.0D, 20, 5, 5);
    }

    private static void sendExampleStatusEffect(Entity entity) {
        EventHelper.sendStatusEffectToNearbyPlayers(entity, null, 10.0D, new MobEffectInstance(Holder.direct(CStatusEffects.RADIANCE.get()), 40));
    }

    private static void summonExampleEntitySpawner(Level level, Vec3 position) {
        EntitySpawner.create(level, Chrysalis.stringId("example"), position);
    }

    private static void registerExampleFluidsForFluidLogging() {
        RegistryHelper.registerFluidForFluidlogging(Fluids.WATER, FluidloggedState.WATER);
        RegistryHelper.registerFluidForFluidlogging(Fluids.LAVA, FluidloggedState.LAVA);
    }

    private static final ColoredDustPlumeParticleOptions EXAMPLE_COLORED_DUST_PLUME_PARTICLES = new ColoredDustPlumeParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), true, true, 1.0F);
    private static final ColoredDirectionalDustParticleOptions EXAMPLE_COLORED_DIRECTIONAL_DUST_PARTICLES = new ColoredDirectionalDustParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), CommonColors.WHITE);
    private static final ColoredPortalParticleOptions EXAMPLE_COLORED_PORTAL_PARTICLES = new ColoredPortalParticleOptions(ComponentHelper.FIRE_COLOR.getRGB(), ComponentHelper.SOUL_FIRE_COLOR.getRGB(), false);
    private static final DustExplosionParticleOptions EXAMPLE_DUST_EXPLOSION_PARTICLES = new DustExplosionParticleOptions(ComponentHelper.FIRE_COLOR.getRGB(), ComponentHelper.SOUL_FIRE_COLOR.getRGB(), true, 1.0F);
    private static final RotatingDustParticleOptions EXAMPLE_ROTATING_DUST_PARTICLES = new RotatingDustParticleOptions(ComponentHelper.MEMORY_FIRE_COLOR.getRGB(), true, true, false, 1.0F);
    private static final SparkleParticleOptions EXAMPLE_SPARKLE_PARTICLES = new SparkleParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false);
    private static final SparkParticleOptions EXAMPLE_SPARK_PARTICLES = new SparkParticleOptions(ComponentHelper.FIRE_COLOR.getRGB(), true, 1.0F);
    private static final SmallPulsationParticleOptions EXAMPLE_SMALL_PULSATION_PARTICLES = new SmallPulsationParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false, Direction.UP.get3DDataValue(), 10);
    private static final LargePulsationParticleOptions EXAMPLE_LARGE_PULSATION_PARTICLES = new LargePulsationParticleOptions(ComponentHelper.CHRYSALIS_COLOR.getRGB(), false, Direction.UP.get3DDataValue(), 20);
    private static final MusicNoteParticleOptions EXAMPLE_MUSIC_NOTE_PARTICLES = new MusicNoteParticleOptions(ComponentHelper.AMETHYST_COLOR.getRGB(), false, true, 1.0F);

    // endregion
}