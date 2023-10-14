package net.sydokiddo.chrysalis.misc.util;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.mixin.util.BrewingRecipeRegistryMixin;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInPowderSnowBucketItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@SuppressWarnings("all")
public class RegistryHelpers {

    public static void init() {}

    // region Potion Recipe Registry

    /**
     * Registry helpers for registering potion recipes.
     **/

    public static void registerBasePotionRecipe(Item ingredient, Potion resultPotion) {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ingredient, resultPotion);
    }

    public static void registerLongPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(startingPotion, Items.REDSTONE, resultPotion);
    }

    public static void registerStrongPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(startingPotion, Items.GLOWSTONE_DUST, resultPotion);
    }

    public static void registerInvertedPotionRecipe(Potion startingPotion, Potion resultPotion) {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(startingPotion, Items.FERMENTED_SPIDER_EYE, resultPotion);
    }

    // endregion

    // region Item Registry

    /**
     * Registry helpers for registering items with specific properties.
     **/

    public static SwordItem registerSword(Tier tier) {
        return new SwordItem(tier, 3, -2.4F, new FabricItemSettings());
    }

    public static PickaxeItem registerPickaxe(Tier tier) {
        return new PickaxeItem(tier, 1, -2.8F, new FabricItemSettings());
    }

    public static AxeItem registerAxe(Tier tier) {
        return new AxeItem(tier, 5.0F, -3.0F, new FabricItemSettings());
    }

    public static ShovelItem registerShovel(Tier tier) {
        return new ShovelItem(tier, 1.5F, -3.0F, new FabricItemSettings());
    }

    public static HoeItem registerHoe(Tier tier) {
        return new HoeItem(tier, -4, 0.0F, new FabricItemSettings());
    }

    public static ArmorItem registerHelmet(ArmorMaterial armorMaterial) {
        return new ArmorItem(armorMaterial, ArmorItem.Type.HELMET, new FabricItemSettings());
    }

    public static ArmorItem registerChestplate(ArmorMaterial armorMaterial) {
        return new ArmorItem(armorMaterial, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
    }

    public static ArmorItem registerLeggings(ArmorMaterial armorMaterial) {
        return new ArmorItem(armorMaterial, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
    }

    public static ArmorItem registerBoots(ArmorMaterial armorMaterial) {
        return new ArmorItem(armorMaterial, ArmorItem.Type.BOOTS, new FabricItemSettings());
    }

    public static RecordItem registerMusicDisc(int redstoneOutput, SoundEvent soundEvent, int musicLength) {
        return new RecordItem(redstoneOutput, soundEvent, new FabricItemSettings().stacksTo(1).rarity(Rarity.RARE), musicLength);
    }

    public static SpawnEggItem registerSpawnEgg(EntityType entityType, int baseColor, int accentColor) {
       return new SpawnEggItem(entityType, baseColor, accentColor, new FabricItemSettings());
    }

    public static MobInContainerItem registerMobInContainer(EntityType entityType, SoundEvent soundEvent, Item returnItem) {
        return new MobInContainerItem(entityType, soundEvent, new FabricItemSettings().stacksTo(1).craftRemainder(returnItem), returnItem);
    }

    public static MobBucketItem registerMobInWaterBucket(EntityType entityType, SoundEvent soundEvent) {
        return new MobBucketItem(entityType, Fluids.WATER, soundEvent, new FabricItemSettings().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static MobBucketItem registerMobInLavaBucket(EntityType entityType, SoundEvent soundEvent) {
        return new MobBucketItem(entityType, Fluids.LAVA, soundEvent, new FabricItemSettings().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static MobBucketItem registerMobInCustomFluidBucket(EntityType entityType, Fluid fluidType, SoundEvent soundEvent) {
        return new MobBucketItem(entityType, fluidType, soundEvent, new FabricItemSettings().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static MobInPowderSnowBucketItem registerMobInPowderSnowBucket(EntityType entityType, SoundEvent soundEvent, Item returnItem) {
        return new MobInPowderSnowBucketItem(entityType, soundEvent, new FabricItemSettings().stacksTo(1).craftRemainder(returnItem), returnItem);
    }

    // endregion

    // region Block Registry

    /**
     * Registry helpers for registering blocks with specific properties.
     **/

    public static ButtonBlock registerStoneButton() {
        return new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F)
        .pushReaction(PushReaction.DESTROY), BlockSetType.STONE, 20, false);
    }

    public static ButtonBlock registerWoodenButton(BlockSetType blockSetType) {
        return new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F)
        .pushReaction(PushReaction.DESTROY), blockSetType, 30, true);
    }

    public static PressurePlateBlock registerStonePressurePlate(MapColor mapColor, BlockSetType blockSetType) {
        return new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(mapColor)
        .forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
        .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), blockSetType);
    }

    public static PressurePlateBlock registerWoodenPressurePlate(MapColor mapColor, BlockSetType blockSetType) {
        return new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(mapColor)
        .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY), blockSetType);
    }

    public static PressurePlateBlock registerFireProofWoodenPressurePlate(MapColor mapColor, BlockSetType blockSetType) {
        return new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(mapColor)
        .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), blockSetType);
    }

    public static LeavesBlock registerLeaves(SoundType soundType, MapColor mapColor) {
        return new LeavesBlock(BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.2F).randomTicks()
        .sound(soundType).noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(RegistryHelpers::never)
        .isViewBlocking(RegistryHelpers::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(RegistryHelpers::never));
    }

    public static RotatedPillarBlock registerLog(MapColor innerColor, MapColor sideColor, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? innerColor : sideColor)
        .instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(soundType).ignitedByLava());
    }

    public static Block registerFireProofLog(MapColor innerColor, MapColor sideColor, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? innerColor : sideColor)
        .instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(soundType));
    }

    public static RotatedPillarBlock registerBlastResistantLog(MapColor innerColor, MapColor sideColor, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? innerColor : sideColor)
        .instrument(NoteBlockInstrument.BASS).strength(2.0F, 6.0F).sound(soundType).ignitedByLava());
    }

    // endregion

    // region Composter and Furnace Values

    public static float
        composter30PercentChance = 0.3F,
        composter50PercentChance = 0.5F,
        composter65PercentChance = 0.65F,
        composter85PercentChance = 0.85F,
        composter100PercentChance = 1.0F
    ;

    public static int
        furnace50Ticks = 50,
        furnace100Ticks = 100,
        furnace150Ticks = 150,
        furnace200Ticks = 200,
        furnace300Ticks = 300,
        furnace1600Ticks = 1600,
        furnace2400Ticks = 2400,
        furnace4000Ticks = 4000,
        furnace16000Ticks = 16000,
        furnace20000Ticks = 20000
    ;

    // endregion

    // region Biome Predicates

    /**
     * Registry helpers for assisting with mob spawning.
     **/

    public static Predicate<BiomeSelectionContext> isValidBiomeForMobSpawning() {
        return context -> !context.getBiomeRegistryEntry().is(ChrysalisTags.WITHOUT_MOB_SPAWNS);
    }

    public static Predicate<BiomeSelectionContext> isOverworld() {
        return context -> context.canGenerateIn(LevelStem.OVERWORLD);
    }

    public static Predicate<BiomeSelectionContext> isNether() {
        return context -> context.canGenerateIn(LevelStem.NETHER);
    }

    public static Predicate<BiomeSelectionContext> isEnd() {
        return context -> context.canGenerateIn(LevelStem.END);
    }

    // endregion

    // region World Generation Properties

    /**
     * Registry helpers for assisting with world generation properties.
     **/

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerConfiguredFeature(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, F feature, FC featureConfiguration) {
        context.register(resourceKey, new ConfiguredFeature<>(feature, featureConfiguration));
    }

    public static void registerPlacedFeature(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placementModifiers) {
        context.register(resourceKey, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature), List.copyOf(placementModifiers)));
    }

    public static void registerPlacedFeature(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
        registerPlacedFeature(context, resourceKey, configuredFeature, List.of(placementModifiers));
    }

    // endregion

    // region Loot Table Registry

    /**
     * Registry helpers for assisting with custom loot tables.
     **/

    public static ResourceLocation registerCustomLootTable(String string) {
        return registerCustomLootTable(new ResourceLocation(string));
    }

    public static final Set<ResourceLocation> LOOT_TABLE_LOCATIONS = Sets.newHashSet();

    public static ResourceLocation registerCustomLootTable(ResourceLocation resourceLocation) {
        if (LOOT_TABLE_LOCATIONS.add(resourceLocation)) {
            return resourceLocation;
        }
        throw new IllegalArgumentException(resourceLocation + " is already a registered built-in loot table");
    }

    // endregion

    // region Miscellanous Properties

    /**
     * Miscellaneous properties.
     **/

    public static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return true;
    }

    public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    public static boolean isBlockStateFree(BlockState blockState) {
        return blockState.is(BlockTags.REPLACEABLE);
    }

    public static ToIntFunction<BlockState> blockStateShouldEmitLight(int lightAmount) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightAmount : 0;
    }

    // endregion

    // region Dispenser Utilities

    /**
     * Dispenser utility methods.
     **/

    public static final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    public static void playDispenserSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1000, blockSource.pos(), 0);
    }

    public static void playDispenserShootingSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1002, blockSource.pos(), 0);
    }

    public static void playDispenserAnimation(BlockSource blockSource, Direction direction) {
        blockSource.level().levelEvent(2000, blockSource.pos(), direction.get3DDataValue());
    }

    // endregion

    // region Namespace Strings

    /**
     * Namespace strings for base Minecraft as well as compatibility with various mods.
     **/

    public static String
        minecraft = "minecraft",
        odyssey = "odyssey",
        endlessencore = "endlessencore",
        auditory = "auditory",
        combatant = "combatant",
        peculia = "peculia",
        spookiar = "spookiar",
        lottablocks = "lottablocks",
        manic = "manic"
    ;

    // endregion

    /**
     * Resource locations for all of the base chest loot tables in the vanilla game.
     **/

    public static final ResourceLocation

        // region Vanilla Chest Loot Tables

        MINESHAFT = new ResourceLocation(minecraft, "chests/abandoned_mineshaft"),
        ANCIENT_CITY = new ResourceLocation(minecraft, "chests/ancient_city"),
        ANCIENT_CITY_ICE_BOX = new ResourceLocation(minecraft, "chests/ancient_city_ice_box"),
        BASTION_BRIDGE = new ResourceLocation(minecraft, "chests/bastion_bridge"),
        BASTION_HOGLIN_STABLE = new ResourceLocation(minecraft, "chests/bastion_hoglin_stable"),
        BASTION_OTHER = new ResourceLocation(minecraft, "chests/bastion_other"),
        BASTION_TREASURE = new ResourceLocation(minecraft, "chests/bastion_treasure"),
        BURIED_TREASURE = new ResourceLocation(minecraft, "chests/buried_treasure"),
        DESERT_TEMPLE = new ResourceLocation(minecraft, "chests/desert_pyramid"),
        END_CITY_TREASURE = new ResourceLocation(minecraft, "chests/end_city_treasure"),
        IGLOO_CHEST = new ResourceLocation(minecraft, "chests/igloo_chest"),
        JUNGLE_TEMPLE = new ResourceLocation(minecraft, "chests/jungle_temple"),
        NETHER_FORTRESS_BRIDGE = new ResourceLocation(minecraft, "chests/nether_bridge"),
        PILLAGER_OUTPOST = new ResourceLocation(minecraft, "chests/pillager_outpost"),
        RUINED_PORTAL = new ResourceLocation(minecraft, "chests/ruined_portal"),
        SHIPWRECK_MAP = new ResourceLocation(minecraft, "chests/shipwreck_map"),
        SHIPWRECK_SUPPLY = new ResourceLocation(minecraft, "chests/shipwreck_supply"),
        SHIPWRECK_TREASURE = new ResourceLocation(minecraft, "chests/shipwreck_treasure"),
        DUNGEON = new ResourceLocation(minecraft, "chests/simple_dungeon"),
        BONUS_CHEST = new ResourceLocation(minecraft, "chests/spawn_bonus_chest"),
        STRONGHOLD_CORRIDOR = new ResourceLocation(minecraft, "chests/stronghold_corridor"),
        STRONGHOLD_CROSSING = new ResourceLocation(minecraft, "chests/stronghold_crossing"),
        STRONGHOLD_LIBRARY = new ResourceLocation(minecraft, "chests/stronghold_library"),
        UNDERWATER_RUIN_BIG = new ResourceLocation(minecraft, "chests/underwater_ruin_big"),
        UNDERWATER_RUIN_SMALL = new ResourceLocation(minecraft, "chests/underwater_ruin_small"),
        WOODLAND_MANSION = new ResourceLocation(minecraft, "chests/woodland_mansion"),
        VILLAGE_ARMORER = new ResourceLocation(minecraft, "chests/village/village_armorer"),
        VILLAGE_BUTCHER = new ResourceLocation(minecraft, "chests/village/village_butcher"),
        VILLAGE_CARTOGRAPHER = new ResourceLocation(minecraft, "chests/village/village_cartographer"),
        VILLAGE_DESERT_HOUSE = new ResourceLocation(minecraft, "chests/village/village_desert_house"),
        VILLAGE_FISHER = new ResourceLocation(minecraft, "chests/village/village_fisher"),
        VILLAGE_FLETCHER = new ResourceLocation(minecraft, "chests/village/village_fletcher"),
        VILLAGE_MASON = new ResourceLocation(minecraft, "chests/village/village_mason"),
        VILLAGE_PLAINS_HOUSE = new ResourceLocation(minecraft, "chests/village/village_plains_house"),
        VILLAGE_SAVANNA_HOUSE = new ResourceLocation(minecraft, "chests/village/village_savanna_house"),
        VILLAGE_TAIGA_HOUSE = new ResourceLocation(minecraft, "chests/village/village_taiga_house"),
        VILLAGE_TANNERY = new ResourceLocation(minecraft, "chests/village/village_tannery"),
        VILLAGE_TEMPLE = new ResourceLocation(minecraft, "chests/village/village_temple"),
        VILLAGE_TOOLSMITH = new ResourceLocation(minecraft, "chests/village/village_toolsmith"),
        VILLAGE_WEAPONSMITH = new ResourceLocation(minecraft, "chests/village/village_weaponsmith"),

        // endregion

        // region Modded Chest Loot Tables

        END_CITY_JUNK = new ResourceLocation(endlessencore, "chests/end_city_junk"),
        END_SHIP = new ResourceLocation(endlessencore, "chests/end_ship"),
        STRONGHOLD_BREWERY = new ResourceLocation(endlessencore, "chests/stronghold_brewery"),
        STRONGHOLD_FORGE = new ResourceLocation(endlessencore, "chests/stronghold_forge"),
        STRONGHOLD_KITCHEN = new ResourceLocation(endlessencore, "chests/stronghold_kitchen"),
        STRONGHOLD_NETHER_PORTAL = new ResourceLocation(endlessencore, "chests/stronghold_nether_portal"),
        STRONGHOLD_PRISON = new ResourceLocation(endlessencore, "chests/stronghold_prison"),
        STRONGHOLD_SEWER = new ResourceLocation(endlessencore, "chests/stronghold_sewer"),

        // endregion

        // region Vanilla Archaeology Loot Tables

        ARCHAEOLOGY_DESERT_TEMPLE = new ResourceLocation(minecraft, "archaeology/desert_pyramid"),
        ARCHAEOLOGY_DESERT_WELL = new ResourceLocation(minecraft, "archaeology/desert_well"),
        ARCHAEOLOGY_COLD_OCEAN_RUIN = new ResourceLocation(minecraft, "archaeology/ocean_ruin_cold"),
        ARCHAEOLOGY_WARM_OCEAN_RUIN = new ResourceLocation(minecraft, "archaeology/ocean_ruin_warm"),
        ARCHAEOLOGY_COMMON_TRAIL_RUINS = new ResourceLocation(minecraft, "archaeology/trail_ruins_common"),
        ARCHAEOLOGY_RARE_TRAIL_RUINS = new ResourceLocation(minecraft, "archaeology/trail_ruins_rare"),

        // endregion

        // region Modded Archaeology Loot Tables

        ARCHAEOLOGY_STRONGHOLD = new ResourceLocation(endlessencore, "archaeology/stronghold"),
        ARCHAEOLOGY_TITAN_NEST = new ResourceLocation(peculia, "archaeology/titan_nest"),

        // endregion

        // region Vanilla Gameplay Loot Tables

        CAT_MORNING_GIFT = new ResourceLocation(minecraft, "gameplay/cat_morning_gift"),
        FISHING = new ResourceLocation(minecraft, "gameplay/fishing"),
        PIGLIN_BARTERING = new ResourceLocation(minecraft, "gameplay/piglin_bartering"),
        SNIFFER_DIGGING = new ResourceLocation(minecraft, "gameplay/sniffer_digging"),
        FISHING_FISH = new ResourceLocation(minecraft, "gameplay/fishing/fish"),
        FISHING_JUNK = new ResourceLocation(minecraft, "gameplay/fishing/junk"),
        FISHING_TREASURE = new ResourceLocation(minecraft, "gameplay/fishing/treasure"),
        HERO_OF_THE_VILLAGE_GIFT_ARMORER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/armorer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_BUTCHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/butcher_gift"),
        HERO_OF_THE_VILLAGE_GIFT_CARTOGRAPHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/cartographer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_CLERIC = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/cleric_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FARMER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/farmer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FISHERMAN = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/fisherman_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FLETCHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/fletcher_gift"),
        HERO_OF_THE_VILLAGE_GIFT_LEATHERWORKER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/leatherworker_gift"),
        HERO_OF_THE_VILLAGE_GIFT_LIBRARIAN = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/librarian_gift"),
        HERO_OF_THE_VILLAGE_GIFT_MASON = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/mason_gift"),
        HERO_OF_THE_VILLAGE_GIFT_SHEPHERD = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/shepherd_gift"),
        HERO_OF_THE_VILLAGE_GIFT_TOOLSMITH = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/toolsmith_gift"),
        HERO_OF_THE_VILLAGE_GIFT_WEAPONSMITH = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/weaponsmith_gift"),

        // endregion

        // region Modded Gameplay Loot Tables

        FLEET_MOULTING = new ResourceLocation(endlessencore, "gameplay/fleet_moulting"),
        GOLDEN_FLEET_MOULTING = new ResourceLocation(endlessencore, "gameplay/golden_fleet_moulting"),

        // endregion

        // region Vanilla Entity Loot Tables

        BLACK_SHEEP = new ResourceLocation(minecraft, "entities/sheep/black"),
        BLUE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/blue"),
        BROWN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/brown"),
        CYAN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/cyan"),
        GRAY_SHEEP = new ResourceLocation(minecraft, "entities/sheep/gray"),
        GREEN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/green"),
        LIGHT_BLUE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/light_blue"),
        LIGHT_GRAY_SHEEP = new ResourceLocation(minecraft, "entities/sheep/light_gray"),
        LIME_SHEEP = new ResourceLocation(minecraft, "entities/sheep/lime"),
        MAGENTA_SHEEP = new ResourceLocation(minecraft, "entities/sheep/magenta"),
        ORANGE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/orange"),
        PINK_SHEEP = new ResourceLocation(minecraft, "entities/sheep/pink"),
        PURPLE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/purple"),
        RED_SHEEP = new ResourceLocation(minecraft, "entities/sheep/red"),
        WHITE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/white"),
        YELLOW_SHEEP = new ResourceLocation(minecraft, "entities/sheep/yellow"),
        ALLAY = new ResourceLocation(minecraft, "entities/allay"),
        ARMOR_STAND = new ResourceLocation(minecraft, "entities/armor_stand"),
        AXOLOTL = new ResourceLocation(minecraft, "entities/axolotl"),
        BAT = new ResourceLocation(minecraft, "entities/bat"),
        BEE = new ResourceLocation(minecraft, "entities/bee"),
        BLAZE = new ResourceLocation(minecraft, "entities/blaze"),
        CAMEL = new ResourceLocation(minecraft, "entities/camel"),
        CAT = new ResourceLocation(minecraft, "entities/cat"),
        CAVE_SPIDER = new ResourceLocation(minecraft, "entities/cave_spider"),
        CHICKEN = new ResourceLocation(minecraft, "entities/chicken"),
        COD = new ResourceLocation(minecraft, "entities/cod"),
        COW = new ResourceLocation(minecraft, "entities/cow"),
        CREEPER = new ResourceLocation(minecraft, "entities/creeper"),
        DOLPHIN = new ResourceLocation(minecraft, "entities/dolphin"),
        DONKEY = new ResourceLocation(minecraft, "entities/donkey"),
        DROWNED = new ResourceLocation(minecraft, "entities/drowned"),
        ELDER_GUARDIAN = new ResourceLocation(minecraft, "entities/elder_guardian"),
        ENDER_DRAGON = new ResourceLocation(minecraft, "entities/ender_dragon"),
        ENDERMAN = new ResourceLocation(minecraft, "entities/enderman"),
        ENDERMITE = new ResourceLocation(minecraft, "entities/endermite"),
        EVOKER = new ResourceLocation(minecraft, "entities/evoker"),
        FOX = new ResourceLocation(minecraft, "entities/fox"),
        FROG = new ResourceLocation(minecraft, "entities/frog"),
        GHAST = new ResourceLocation(minecraft, "entities/ghast"),
        GIANT = new ResourceLocation(minecraft, "entities/giant"),
        GLOW_SQUID = new ResourceLocation(minecraft, "entities/glow_squid"),
        GOAT = new ResourceLocation(minecraft, "entities/goat"),
        GUARDIAN = new ResourceLocation(minecraft, "entities/guardian"),
        HOGLIN = new ResourceLocation(minecraft, "entities/hoglin"),
        HORSE = new ResourceLocation(minecraft, "entities/horse"),
        HUSK = new ResourceLocation(minecraft, "entities/husk"),
        ILLUSIONER = new ResourceLocation(minecraft, "entities/illusioner"),
        IRON_GOLEM = new ResourceLocation(minecraft, "entities/iron_golem"),
        LLAMA = new ResourceLocation(minecraft, "entities/llama"),
        MAGMA_CUBE = new ResourceLocation(minecraft, "entities/magma_cube"),
        MOOSHROOM = new ResourceLocation(minecraft, "entities/mooshroom"),
        MULE = new ResourceLocation(minecraft, "entities/mule"),
        OCELOT = new ResourceLocation(minecraft, "entities/ocelot"),
        PANDA = new ResourceLocation(minecraft, "entities/panda"),
        PARROT = new ResourceLocation(minecraft, "entities/parrot"),
        PHANTOM = new ResourceLocation(minecraft, "entities/phantom"),
        PIG = new ResourceLocation(minecraft, "entities/pig"),
        PIGLIN = new ResourceLocation(minecraft, "entities/piglin"),
        PIGLIN_BRUTE = new ResourceLocation(minecraft, "entities/piglin_brute"),
        PILLAGER = new ResourceLocation(minecraft, "entities/pillager"),
        PLAYER = new ResourceLocation(minecraft, "entities/player"),
        POLAR_BEAR = new ResourceLocation(minecraft, "entities/polar_bear"),
        PUFFERFISH = new ResourceLocation(minecraft, "entities/pufferfish"),
        RABBIT = new ResourceLocation(minecraft, "entities/rabbit"),
        RAVAGER = new ResourceLocation(minecraft, "entities/ravager"),
        SALMON = new ResourceLocation(minecraft, "entities/salmon"),
        SHEEP = new ResourceLocation(minecraft, "entities/sheep"),
        SHULKER = new ResourceLocation(minecraft, "entities/shulker"),
        SILVERFISH = new ResourceLocation(minecraft, "entities/silverfish"),
        SKELETON = new ResourceLocation(minecraft, "entities/skeleton"),
        SKELETON_HORSE = new ResourceLocation(minecraft, "entities/skeleton_horse"),
        SLIME = new ResourceLocation(minecraft, "entities/slime"),
        SNIFFER = new ResourceLocation(minecraft, "entities/sniffer"),
        SNOW_GOLEM = new ResourceLocation(minecraft, "entities/snow_golem"),
        SPIDER = new ResourceLocation(minecraft, "entities/spider"),
        SQUID = new ResourceLocation(minecraft, "entities/squid"),
        STRAY = new ResourceLocation(minecraft, "entities/stray"),
        STRIDER = new ResourceLocation(minecraft, "entities/strider"),
        TADPOLE = new ResourceLocation(minecraft, "entities/tadpole"),
        TRADER_LLAMA = new ResourceLocation(minecraft, "entities/trader_llama"),
        TROPICAL_FISH = new ResourceLocation(minecraft, "entities/tropical_fish"),
        TURTLE = new ResourceLocation(minecraft, "entities/turtle"),
        VEX = new ResourceLocation(minecraft, "entities/vex"),
        VILLAGER = new ResourceLocation(minecraft, "entities/villager"),
        VINDICATOR = new ResourceLocation(minecraft, "entities/vindicator"),
        WANDERING_TRADER = new ResourceLocation(minecraft, "entities/wandering_trader"),
        WARDEN = new ResourceLocation(minecraft, "entities/warden"),
        WITCH = new ResourceLocation(minecraft, "entities/witch"),
        WITHER = new ResourceLocation(minecraft, "entities/wither"),
        WITHER_SKELETON = new ResourceLocation(minecraft, "entities/wither_skeleton"),
        WOLF = new ResourceLocation(minecraft, "entities/wolf"),
        ZOGLIN = new ResourceLocation(minecraft, "entities/zoglin"),
        ZOMBIE = new ResourceLocation(minecraft, "entities/zombie"),
        ZOMBIE_HORSE = new ResourceLocation(minecraft, "entities/zombie_horse"),
        ZOMBIE_VILLAGER = new ResourceLocation(minecraft, "entities/zombie_villager"),
        ZOMBIFIED_PIGLIN = new ResourceLocation(minecraft, "entities/zombified_piglin"),

        // endregion

        // region Modded Entity Loot Tables

        FLEET = new ResourceLocation(endlessencore, "entities/fleet"),
        FLEET_GRUB = new ResourceLocation(endlessencore, "entities/fleet_grub"),
        MUSE = new ResourceLocation(endlessencore, "entities/muse"),
        RENDLING = new ResourceLocation(endlessencore, "entities/rendling"),
        ENDERSENT = new ResourceLocation(endlessencore, "entities/endersent"),
        ENDERBYTE = new ResourceLocation(endlessencore, "entities/enderbyte"),

        MOLDSPAWN = new ResourceLocation(peculia, "entities/moldspawn"),
        MOLDLIN = new ResourceLocation(peculia, "entities/moldlin"),
        BABY_TITAN = new ResourceLocation(peculia, "entities/baby_titan"),

        GHOST = new ResourceLocation(spookiar, "entities/ghost"),
        WISP = new ResourceLocation(spookiar, "entities/wisp"),
        PRIME_SPIRIT = new ResourceLocation(spookiar, "entities/prime_spirit");

        // endregion
    ;
}