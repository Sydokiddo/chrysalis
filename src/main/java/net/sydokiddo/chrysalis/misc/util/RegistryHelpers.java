package net.sydokiddo.chrysalis.misc.util;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.mixin.util.BrewingRecipeRegistryMixin;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@SuppressWarnings("all")
public class RegistryHelpers {

    public static void init() {}

    // Potion Recipes

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

    // Block Registries

    public static ButtonBlock registerStoneButton() {
        return new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5f)
        .pushReaction(PushReaction.DESTROY), BlockSetType.STONE, 20, false);
    }

    public static ButtonBlock registerWoodenButton(BlockSetType blockSetType) {
        return new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5f)
        .pushReaction(PushReaction.DESTROY), blockSetType, 30, true);
    }

    public static LeavesBlock registerLeaves(SoundType soundType) {
        return new LeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2f).randomTicks()
        .sound(soundType).noOcclusion().isValidSpawn(RegistryHelpers::canSpawnOnLeaves).isSuffocating(RegistryHelpers::never)
        .isViewBlocking(RegistryHelpers::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(RegistryHelpers::never));
    }

    public static RotatedPillarBlock registerLog(MapColor mapColor, MapColor mapColor2, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? mapColor : mapColor2)
        .instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(soundType).ignitedByLava());
    }

    public static Block registerFireProofLog(MapColor mapColor, MapColor mapColor2, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? mapColor : mapColor2)
        .instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(soundType));
    }

    // Mob Spawning

    public static Boolean canSpawnOnLeaves(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }

    public static Predicate<BiomeSelectionContext> isValidBiomeForMobSpawning() {
        return context -> !context.isIn(ChrysalisTags.WITHOUT_MOB_SPAWNS);
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

    // World Generation

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

    // Namespace Strings

    private static String minecraft = "minecraft";

    // Compatibility Namespace Strings

    private static String endlessencore = "endlessencore";
    private static String peculia = "peculia";
    private static String spookiar = "spookiar";
    private static String odyssey = "odyssey";
    private static String combatant = "combatant";
    private static String auditory = "auditory";
    private static String lottablocks = "lottablocks";

    // Chest Loot Tables

    public static final ResourceLocation MINESHAFT = new ResourceLocation(minecraft, "chests/abandoned_mineshaft");
    public static final ResourceLocation ANCIENT_CITY = new ResourceLocation(minecraft, "chests/ancient_city");
    public static final ResourceLocation ANCIENT_CITY_ICE_BOX = new ResourceLocation(minecraft, "chests/ancient_city_ice_box");
    public static final ResourceLocation BASTION_BRIDGE = new ResourceLocation(minecraft, "chests/bastion_bridge");
    public static final ResourceLocation BASTION_HOGLIN_STABLE = new ResourceLocation(minecraft, "chests/bastion_hoglin_stable");
    public static final ResourceLocation BASTION_OTHER = new ResourceLocation(minecraft, "chests/bastion_other");
    public static final ResourceLocation BASTION_TREASURE = new ResourceLocation(minecraft, "chests/bastion_treasure");
    public static final ResourceLocation BURIED_TREASURE = new ResourceLocation(minecraft, "chests/buried_treasure");
    public static final ResourceLocation DESERT_TEMPLE = new ResourceLocation(minecraft, "chests/desert_pyramid");
    public static final ResourceLocation END_CITY_TREASURE = new ResourceLocation(minecraft, "chests/end_city_treasure");
    public static final ResourceLocation IGLOO_CHEST = new ResourceLocation(minecraft, "chests/igloo_chest");
    public static final ResourceLocation JUNGLE_TEMPLE = new ResourceLocation(minecraft, "chests/jungle_temple");
    public static final ResourceLocation NETHER_FORTRESS_BRIDGE = new ResourceLocation(minecraft, "chests/nether_bridge");
    public static final ResourceLocation PILLAGER_OUTPOST = new ResourceLocation(minecraft, "chests/pillager_outpost");
    public static final ResourceLocation RUINED_PORTAL = new ResourceLocation(minecraft, "chests/ruined_portal");
    public static final ResourceLocation SHIPWRECK_MAP = new ResourceLocation(minecraft, "chests/shipwreck_map");
    public static final ResourceLocation SHIPWRECK_SUPPLY = new ResourceLocation(minecraft, "chests/shipwreck_supply");
    public static final ResourceLocation SHIPWRECK_TREASURE = new ResourceLocation(minecraft, "chests/shipwreck_treasure");
    public static final ResourceLocation DUNGEON = new ResourceLocation(minecraft, "chests/simple_dungeon");
    public static final ResourceLocation BONUS_CHEST = new ResourceLocation(minecraft, "chests/spawn_bonus_chest");
    public static final ResourceLocation STRONGHOLD_CORRIDOR = new ResourceLocation(minecraft, "chests/stronghold_corridor");
    public static final ResourceLocation STRONGHOLD_CROSSING = new ResourceLocation(minecraft, "chests/stronghold_crossing");
    public static final ResourceLocation STRONGHOLD_LIBRARY = new ResourceLocation(minecraft, "chests/stronghold_library");
    public static final ResourceLocation UNDERWATER_RUIN_BIG = new ResourceLocation(minecraft, "chests/underwater_ruin_big");
    public static final ResourceLocation UNDERWATER_RUIN_SMALL = new ResourceLocation(minecraft, "chests/underwater_ruin_small");
    public static final ResourceLocation WOODLAND_MANSION = new ResourceLocation(minecraft, "chests/woodland_mansion");
    public static final ResourceLocation VILLAGE_ARMORER = new ResourceLocation(minecraft, "chests/village/village_armorer");
    public static final ResourceLocation VILLAGE_BUTCHER = new ResourceLocation(minecraft, "chests/village/village_butcher");
    public static final ResourceLocation VILLAGE_CARTOGRAPHER = new ResourceLocation(minecraft, "chests/village/village_cartographer");
    public static final ResourceLocation VILLAGE_DESERT_HOUSE = new ResourceLocation(minecraft, "chests/village/village_desert_house");
    public static final ResourceLocation VILLAGE_FISHER = new ResourceLocation(minecraft, "chests/village/village_fisher");
    public static final ResourceLocation VILLAGE_FLETCHER = new ResourceLocation(minecraft, "chests/village/village_fletcher");
    public static final ResourceLocation VILLAGE_MASON = new ResourceLocation(minecraft, "chests/village/village_mason");
    public static final ResourceLocation VILLAGE_PLAINS_HOUSE = new ResourceLocation(minecraft, "chests/village/village_plains_house");
    public static final ResourceLocation VILLAGE_SAVANNA_HOUSE = new ResourceLocation(minecraft, "chests/village/village_savanna_house");
    public static final ResourceLocation VILLAGE_TAIGA_HOUSE = new ResourceLocation(minecraft, "chests/village/village_taiga_house");
    public static final ResourceLocation VILLAGE_TANNERY = new ResourceLocation(minecraft, "chests/village/village_tannery");
    public static final ResourceLocation VILLAGE_TEMPLE = new ResourceLocation(minecraft, "chests/village/village_temple");
    public static final ResourceLocation VILLAGE_TOOLSMITH = new ResourceLocation(minecraft, "chests/village/village_toolsmith");
    public static final ResourceLocation VILLAGE_WEAPONSMITH = new ResourceLocation(minecraft, "chests/village/village_weaponsmith");

    // Compatibility

    public static final ResourceLocation END_CITY_JUNK = new ResourceLocation(endlessencore, "chests/end_city_junk");
    public static final ResourceLocation END_SHIP = new ResourceLocation(endlessencore, "chests/end_ship");
    public static final ResourceLocation STRONGHOLD_BREWERY = new ResourceLocation(endlessencore, "chests/stronghold_brewery");
    public static final ResourceLocation STRONGHOLD_FORGE = new ResourceLocation(endlessencore, "chests/stronghold_forge");
    public static final ResourceLocation STRONGHOLD_KITCHEN = new ResourceLocation(endlessencore, "chests/stronghold_kitchen");
    public static final ResourceLocation STRONGHOLD_NETHER_PORTAL = new ResourceLocation(endlessencore, "chests/stronghold_nether_portal");
    public static final ResourceLocation STRONGHOLD_PRISON = new ResourceLocation(endlessencore, "chests/stronghold_prison");
    public static final ResourceLocation STRONGHOLD_SEWER = new ResourceLocation(endlessencore, "chests/stronghold_sewer");

    // Archaeology Loot Tables

    public static final ResourceLocation ARCHAEOLOGY_DESERT_TEMPLE = new ResourceLocation(minecraft, "archaeology/desert_pyramid");
    public static final ResourceLocation ARCHAEOLOGY_DESERT_WELL = new ResourceLocation(minecraft, "archaeology/desert_well");
    public static final ResourceLocation ARCHAEOLOGY_COLD_OCEAN_RUIN = new ResourceLocation(minecraft, "archaeology/ocean_ruin_cold");
    public static final ResourceLocation ARCHAEOLOGY_WARM_OCEAN_RUIN = new ResourceLocation(minecraft, "archaeology/ocean_ruin_warm");
    public static final ResourceLocation ARCHAEOLOGY_COMMON_TRAIL_RUINS = new ResourceLocation(minecraft, "archaeology/trail_ruins_common");
    public static final ResourceLocation ARCHAEOLOGY_RARE_TRAIL_RUINS = new ResourceLocation(minecraft, "archaeology/trail_ruins_rare");

    // Compatibility

    public static final ResourceLocation ARCHAEOLOGY_STRONGHOLD = new ResourceLocation(endlessencore, "archaeology/stronghold");

    // Gameplay Loot Tables

    public static final ResourceLocation CAT_MORNING_GIFT = new ResourceLocation(minecraft, "gameplay/cat_morning_gift");
    public static final ResourceLocation FISHING = new ResourceLocation(minecraft, "gameplay/fishing");
    public static final ResourceLocation PIGLIN_BARTERING = new ResourceLocation(minecraft, "gameplay/piglin_bartering");
    public static final ResourceLocation SNIFFER_DIGGING = new ResourceLocation(minecraft, "gameplay/sniffer_digging");
    public static final ResourceLocation FISHING_FISH = new ResourceLocation(minecraft, "gameplay/fishing/fish");
    public static final ResourceLocation FISHING_JUNK = new ResourceLocation(minecraft, "gameplay/fishing/junk");
    public static final ResourceLocation FISHING_TREASURE = new ResourceLocation(minecraft, "gameplay/fishing/treasure");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_ARMORER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/armorer_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_BUTCHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/butcher_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_CARTOGRAPHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/cartographer_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_CLERIC = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/cleric_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_FARMER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/farmer_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_FISHERMAN = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/fisherman_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_FLETCHER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/fletcher_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_LEATHERWORKER = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/leatherworker_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_LIBRARIAN = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/librarian_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_MASON = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/mason_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_SHEPHERD = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/shepherd_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_TOOLSMITH = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/toolsmith_gift");
    public static final ResourceLocation HERO_OF_THE_VILLAGE_GIFT_WEAPONSMITH = new ResourceLocation(minecraft, "gameplay/hero_of_the_village/weaponsmith_gift");

    // Compatibility

    public static final ResourceLocation FLEET_MOULTING = new ResourceLocation(endlessencore, "gameplay/fleet_moulting");
    public static final ResourceLocation GOLDEN_FLEET_MOULTING = new ResourceLocation(endlessencore, "gameplay/golden_fleet_moulting");

    // Entity Loot Tables

    public static final ResourceLocation BLACK_SHEEP = new ResourceLocation(minecraft, "entities/sheep/black");
    public static final ResourceLocation BLUE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/blue");
    public static final ResourceLocation BROWN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/brown");
    public static final ResourceLocation CYAN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/cyan");
    public static final ResourceLocation GRAY_SHEEP = new ResourceLocation(minecraft, "entities/sheep/gray");
    public static final ResourceLocation GREEN_SHEEP = new ResourceLocation(minecraft, "entities/sheep/green");
    public static final ResourceLocation LIGHT_BLUE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/light_blue");
    public static final ResourceLocation LIGHT_GRAY_SHEEP = new ResourceLocation(minecraft, "entities/sheep/light_gray");
    public static final ResourceLocation LIME_SHEEP = new ResourceLocation(minecraft, "entities/sheep/lime");
    public static final ResourceLocation MAGENTA_SHEEP = new ResourceLocation(minecraft, "entities/sheep/magenta");
    public static final ResourceLocation ORANGE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/orange");
    public static final ResourceLocation PINK_SHEEP = new ResourceLocation(minecraft, "entities/sheep/pink");
    public static final ResourceLocation PURPLE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/purple");
    public static final ResourceLocation RED_SHEEP = new ResourceLocation(minecraft, "entities/sheep/red");
    public static final ResourceLocation WHITE_SHEEP = new ResourceLocation(minecraft, "entities/sheep/white");
    public static final ResourceLocation YELLOW_SHEEP = new ResourceLocation(minecraft, "entities/sheep/yellow");
    public static final ResourceLocation ALLAY = new ResourceLocation(minecraft, "entities/allay");
    public static final ResourceLocation ARMOR_STAND = new ResourceLocation(minecraft, "entities/armor_stand");
    public static final ResourceLocation AXOLOTL = new ResourceLocation(minecraft, "entities/axolotl");
    public static final ResourceLocation BAT = new ResourceLocation(minecraft, "entities/bat");
    public static final ResourceLocation BEE = new ResourceLocation(minecraft, "entities/bee");
    public static final ResourceLocation BLAZE = new ResourceLocation(minecraft, "entities/blaze");
    public static final ResourceLocation CAMEL = new ResourceLocation(minecraft, "entities/camel");
    public static final ResourceLocation CAT = new ResourceLocation(minecraft, "entities/cat");
    public static final ResourceLocation CAVE_SPIDER = new ResourceLocation(minecraft, "entities/cave_spider");
    public static final ResourceLocation CHICKEN = new ResourceLocation(minecraft, "entities/chicken");
    public static final ResourceLocation COD = new ResourceLocation(minecraft, "entities/cod");
    public static final ResourceLocation COW = new ResourceLocation(minecraft, "entities/cow");
    public static final ResourceLocation CREEPER = new ResourceLocation(minecraft, "entities/creeper");
    public static final ResourceLocation DOLPHIN = new ResourceLocation(minecraft, "entities/dolphin");
    public static final ResourceLocation DONKEY = new ResourceLocation(minecraft, "entities/donkey");
    public static final ResourceLocation DROWNED = new ResourceLocation(minecraft, "entities/drowned");
    public static final ResourceLocation ELDER_GUARDIAN = new ResourceLocation(minecraft, "entities/elder_guardian");
    public static final ResourceLocation ENDER_DRAGON = new ResourceLocation(minecraft, "entities/ender_dragon");
    public static final ResourceLocation ENDERMAN = new ResourceLocation(minecraft, "entities/enderman");
    public static final ResourceLocation ENDERMITE = new ResourceLocation(minecraft, "entities/endermite");
    public static final ResourceLocation EVOKER = new ResourceLocation(minecraft, "entities/evoker");
    public static final ResourceLocation FOX = new ResourceLocation(minecraft, "entities/fox");
    public static final ResourceLocation FROG = new ResourceLocation(minecraft, "entities/frog");
    public static final ResourceLocation GHAST = new ResourceLocation(minecraft, "entities/ghast");
    public static final ResourceLocation GIANT = new ResourceLocation(minecraft, "entities/giant");
    public static final ResourceLocation GLOW_SQUID = new ResourceLocation(minecraft, "entities/glow_squid");
    public static final ResourceLocation GOAT = new ResourceLocation(minecraft, "entities/goat");
    public static final ResourceLocation GUARDIAN = new ResourceLocation(minecraft, "entities/guardian");
    public static final ResourceLocation HOGLIN = new ResourceLocation(minecraft, "entities/hoglin");
    public static final ResourceLocation HORSE = new ResourceLocation(minecraft, "entities/horse");
    public static final ResourceLocation HUSK = new ResourceLocation(minecraft, "entities/husk");
    public static final ResourceLocation ILLUSIONER = new ResourceLocation(minecraft, "entities/illusioner");
    public static final ResourceLocation IRON_GOLEM = new ResourceLocation(minecraft, "entities/iron_golem");
    public static final ResourceLocation LLAMA = new ResourceLocation(minecraft, "entities/llama");
    public static final ResourceLocation MAGMA_CUBE = new ResourceLocation(minecraft, "entities/magma_cube");
    public static final ResourceLocation MOOSHROOM = new ResourceLocation(minecraft, "entities/mooshroom");
    public static final ResourceLocation MULE = new ResourceLocation(minecraft, "entities/mule");
    public static final ResourceLocation OCELOT = new ResourceLocation(minecraft, "entities/ocelot");
    public static final ResourceLocation PANDA = new ResourceLocation(minecraft, "entities/panda");
    public static final ResourceLocation PARROT = new ResourceLocation(minecraft, "entities/parrot");
    public static final ResourceLocation PHANTOM = new ResourceLocation(minecraft, "entities/phantom");
    public static final ResourceLocation PIG = new ResourceLocation(minecraft, "entities/pig");
    public static final ResourceLocation PIGLIN = new ResourceLocation(minecraft, "entities/piglin");
    public static final ResourceLocation PIGLIN_BRUTE = new ResourceLocation(minecraft, "entities/piglin_brute");
    public static final ResourceLocation PILLAGER = new ResourceLocation(minecraft, "entities/pillager");
    public static final ResourceLocation PLAYER = new ResourceLocation(minecraft, "entities/player");
    public static final ResourceLocation POLAR_BEAR = new ResourceLocation(minecraft, "entities/polar_bear");
    public static final ResourceLocation PUFFERFISH = new ResourceLocation(minecraft, "entities/pufferfish");
    public static final ResourceLocation RABBIT = new ResourceLocation(minecraft, "entities/rabbit");
    public static final ResourceLocation RAVAGER = new ResourceLocation(minecraft, "entities/ravager");
    public static final ResourceLocation SALMON = new ResourceLocation(minecraft, "entities/salmon");
    public static final ResourceLocation SHEEP = new ResourceLocation(minecraft, "entities/sheep");
    public static final ResourceLocation SHULKER = new ResourceLocation(minecraft, "entities/shulker");
    public static final ResourceLocation SILVERFISH = new ResourceLocation(minecraft, "entities/silverfish");
    public static final ResourceLocation SKELETON = new ResourceLocation(minecraft, "entities/skeleton");
    public static final ResourceLocation SKELETON_HORSE = new ResourceLocation(minecraft, "entities/skeleton_horse");
    public static final ResourceLocation SLIME = new ResourceLocation(minecraft, "entities/slime");
    public static final ResourceLocation SNIFFER = new ResourceLocation(minecraft, "entities/sniffer");
    public static final ResourceLocation SNOW_GOLEM = new ResourceLocation(minecraft, "entities/snow_golem");
    public static final ResourceLocation SPIDER = new ResourceLocation(minecraft, "entities/spider");
    public static final ResourceLocation SQUID = new ResourceLocation(minecraft, "entities/squid");
    public static final ResourceLocation STRAY = new ResourceLocation(minecraft, "entities/stray");
    public static final ResourceLocation STRIDER = new ResourceLocation(minecraft, "entities/strider");
    public static final ResourceLocation TADPOLE = new ResourceLocation(minecraft, "entities/tadpole");
    public static final ResourceLocation TRADER_LLAMA = new ResourceLocation(minecraft, "entities/trader_llama");
    public static final ResourceLocation TROPICAL_FISH = new ResourceLocation(minecraft, "entities/tropical_fish");
    public static final ResourceLocation TURTLE = new ResourceLocation(minecraft, "entities/turtle");
    public static final ResourceLocation VEX = new ResourceLocation(minecraft, "entities/vex");
    public static final ResourceLocation VILLAGER = new ResourceLocation(minecraft, "entities/villager");
    public static final ResourceLocation VINDICATOR = new ResourceLocation(minecraft, "entities/vindicator");
    public static final ResourceLocation WANDERING_TRADER = new ResourceLocation(minecraft, "entities/wandering_trader");
    public static final ResourceLocation WARDEN = new ResourceLocation(minecraft, "entities/warden");
    public static final ResourceLocation WITCH = new ResourceLocation(minecraft, "entities/witch");
    public static final ResourceLocation WITHER = new ResourceLocation(minecraft, "entities/wither");
    public static final ResourceLocation WITHER_SKELETON = new ResourceLocation(minecraft, "entities/wither_skeleton");
    public static final ResourceLocation WOLF = new ResourceLocation(minecraft, "entities/wolf");
    public static final ResourceLocation ZOGLIN = new ResourceLocation(minecraft, "entities/zoglin");
    public static final ResourceLocation ZOMBIE = new ResourceLocation(minecraft, "entities/zombie");
    public static final ResourceLocation ZOMBIE_HORSE = new ResourceLocation(minecraft, "entities/zombie_horse");
    public static final ResourceLocation ZOMBIE_VILLAGER = new ResourceLocation(minecraft, "entities/zombie_villager");
    public static final ResourceLocation ZOMBIFIED_PIGLIN = new ResourceLocation(minecraft, "entities/zombified_piglin");

    // Compatibility

    public static final ResourceLocation ENDERSENT = new ResourceLocation(endlessencore, "entities/endersent");
    public static final ResourceLocation FLEET = new ResourceLocation(endlessencore, "entities/fleet");
    public static final ResourceLocation FLEET_GRUB = new ResourceLocation(endlessencore, "entities/fleet_grub");
    public static final ResourceLocation MUSE = new ResourceLocation(endlessencore, "entities/muse");
    public static final ResourceLocation RENDLING = new ResourceLocation(endlessencore, "entities/rendling");

    public static final ResourceLocation MOLDSPAWN = new ResourceLocation(peculia, "entities/moldspawn");
    public static final ResourceLocation MOLDLIN = new ResourceLocation(peculia, "entities/moldlin");
    public static final ResourceLocation BABY_TITAN = new ResourceLocation(peculia, "entities/baby_titan");

    public static final ResourceLocation GHOST = new ResourceLocation(spookiar, "entities/ghost");
    public static final ResourceLocation WISP = new ResourceLocation(spookiar, "entities/wisp");

    // Custom Loot Tables

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

    // Misc

    public static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return true;
    }

    public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    public static boolean isBlockStateFree(BlockState blockState) {
        return blockState.is(BlockTags.REPLACEABLE);
    }

    public static ToIntFunction<BlockState> blockStateShouldEmitLight(int i) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) != false ? i : 0;
    }

    // Dispensers

    public static final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    public static void playDispenserSound(BlockSource blockSource) {
        blockSource.getLevel().levelEvent(1000, blockSource.getPos(), 0);
    }

    public static void playDispenserAnimation(BlockSource blockSource, Direction direction) {
        blockSource.getLevel().levelEvent(2000, blockSource.getPos(), direction.get3DDataValue());
    }
}
