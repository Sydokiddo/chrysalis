package net.sydokiddo.chrysalis.misc.util.helpers;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.mixin.content.registry.BrewingRecipeRegistryBuilderMixin;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.registry.items.custom_items.CSpawnEggItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInFluidBucketItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInPowderSnowBucketItem;
import java.util.List;

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

    public static SwordItem registerSword(Tier tier) {
        return new SwordItem(tier,
                new Item.Properties().attributes(SwordItem.createAttributes(tier, 3, -2.4F))
        );
    }

    public static PickaxeItem registerPickaxe(Tier tier) {
        return new PickaxeItem(tier,
                new Item.Properties().attributes(PickaxeItem.createAttributes(tier, 1, -2.8F))
        );
    }

    public static AxeItem registerAxe(Tier tier) {
        return new AxeItem(tier,
                new Item.Properties().attributes(AxeItem.createAttributes(tier, 5, -3))
        );
    }

    public static ShovelItem registerShovel(Tier tier) {
        return new ShovelItem(tier,
                new Item.Properties().attributes(ShovelItem.createAttributes(tier, 1.5F, -3))
        );
    }

    public static HoeItem registerHoe(Tier tier) {
        return new HoeItem(tier,
                new Item.Properties().attributes(HoeItem.createAttributes(tier, -4, 0))
        );
    }

    public static ArmorItem registerHelmet(ArmorMaterial armorMaterial) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.HELMET,
                new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(5))
        );
    }

    public static ArmorItem registerChestplate(ArmorMaterial armorMaterial) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.CHESTPLATE,
                new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5))
        );
    }

    public static ArmorItem registerLeggings(ArmorMaterial armorMaterial) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.LEGGINGS,
                new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(5))
        );
    }

    public static ArmorItem registerBoots(ArmorMaterial armorMaterial) {
        return new ArmorItem(Holder.direct(armorMaterial), ArmorItem.Type.BOOTS,
                new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(5))
        );
    }

    public static Item registerMusicDisc(int redstoneOutput, ResourceKey<JukeboxSong> song) {
        return new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(song));
    }

    public static CSpawnEggItem registerSpawnEgg(EntityType entityType, int baseColor, int spotsColor, EntityType mobOffspring) {
       return new CSpawnEggItem(entityType, baseColor, spotsColor, mobOffspring, new Item.Properties());
    }

    public static MobInContainerItem registerMobInContainer(EntityType entityType, SoundEvent soundEvent, Item returnItem) {
        return new MobInContainerItem(entityType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(returnItem), returnItem);
    }

    public static MobInFluidBucketItem registerMobInFluidContainer(EntityType entityType, Fluid fluidType, SoundEvent soundEvent) {
        return new MobInFluidBucketItem(entityType, fluidType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static MobInPowderSnowBucketItem registerMobInPowderSnowBucket(EntityType entityType, SoundEvent soundEvent) {
        return new MobInPowderSnowBucketItem(entityType, soundEvent, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET), Items.BUCKET);
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
        if (Sets.newHashSet().add(resourceLocation)) {
            return resourceLocation;
        }
        throw new IllegalArgumentException(resourceLocation + " is already a registered built-in loot table");
    }

    // endregion

    // region Misc Registry

    /**
     * Miscellaneous registry helpers.
     **/

    // TODO i think these arent required anymore, arent features fully data driven now?

//    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerConfiguredFeature(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, F feature, FC featureConfiguration) {
//        context.register(resourceKey, new ConfiguredFeature<>(feature, featureConfiguration));
//    }
//
//    public static void registerPlacedFeature(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
//        registerPlacedFeature(context, resourceKey, configuredFeature, List.of(placementModifiers));
//    }
//
//    public static void registerPlacedFeature(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placementModifiers) {
//        context.register(resourceKey, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature), List.copyOf(placementModifiers)));
//    }

    // endregion

    // region Loot Table Resource Locations

    /**
     * Resource locations for all of the base loot tables in the vanilla game.
     **/

    public static String minecraft = "minecraft";

    public static final ResourceLocation

        // region Archaeology Loot Tables

        ARCHAEOLOGY_DESERT_TEMPLE = ResourceLocation.withDefaultNamespace("archaeology/desert_pyramid"),
        ARCHAEOLOGY_DESERT_WELL = ResourceLocation.withDefaultNamespace("archaeology/desert_well"),
        ARCHAEOLOGY_COLD_OCEAN_RUIN = ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_cold"),
        ARCHAEOLOGY_WARM_OCEAN_RUIN = ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_warm"),
        ARCHAEOLOGY_COMMON_TRAIL_RUINS = ResourceLocation.withDefaultNamespace("archaeology/trail_ruins_common"),
        ARCHAEOLOGY_RARE_TRAIL_RUINS = ResourceLocation.withDefaultNamespace("archaeology/trail_ruins_rare"),

        // endregion

        // region Chest Loot Tables

        MINESHAFT = ResourceLocation.withDefaultNamespace("chests/abandoned_mineshaft"),
        ANCIENT_CITY = ResourceLocation.withDefaultNamespace("chests/ancient_city"),
        ANCIENT_CITY_ICE_BOX = ResourceLocation.withDefaultNamespace("chests/ancient_city_ice_box"),
        BASTION_BRIDGE = ResourceLocation.withDefaultNamespace("chests/bastion_bridge"),
        BASTION_HOGLIN_STABLE = ResourceLocation.withDefaultNamespace("chests/bastion_hoglin_stable"),
        BASTION_OTHER = ResourceLocation.withDefaultNamespace("chests/bastion_other"),
        BASTION_TREASURE = ResourceLocation.withDefaultNamespace("chests/bastion_treasure"),
        BURIED_TREASURE = ResourceLocation.withDefaultNamespace("chests/buried_treasure"),
        DESERT_TEMPLE = ResourceLocation.withDefaultNamespace("chests/desert_pyramid"),
        END_CITY_TREASURE = ResourceLocation.withDefaultNamespace("chests/end_city_treasure"),
        IGLOO_CHEST = ResourceLocation.withDefaultNamespace("chests/igloo_chest"),
        JUNGLE_TEMPLE = ResourceLocation.withDefaultNamespace("chests/jungle_temple"),
        JUNGLE_TEMPLE_DISPENSER = ResourceLocation.withDefaultNamespace("chests/jungle_temple_dispenser"),
        NETHER_FORTRESS_BRIDGE = ResourceLocation.withDefaultNamespace("chests/nether_bridge"),
        PILLAGER_OUTPOST = ResourceLocation.withDefaultNamespace("chests/pillager_outpost"),
        RUINED_PORTAL = ResourceLocation.withDefaultNamespace("chests/ruined_portal"),
        SHIPWRECK_MAP = ResourceLocation.withDefaultNamespace("chests/shipwreck_map"),
        SHIPWRECK_SUPPLY = ResourceLocation.withDefaultNamespace("chests/shipwreck_supply"),
        SHIPWRECK_TREASURE = ResourceLocation.withDefaultNamespace("chests/shipwreck_treasure"),
        DUNGEON = ResourceLocation.withDefaultNamespace("chests/simple_dungeon"),
        BONUS_CHEST = ResourceLocation.withDefaultNamespace("chests/spawn_bonus_chest"),
        STRONGHOLD_CORRIDOR = ResourceLocation.withDefaultNamespace("chests/stronghold_corridor"),
        STRONGHOLD_CROSSING = ResourceLocation.withDefaultNamespace("chests/stronghold_crossing"),
        STRONGHOLD_LIBRARY = ResourceLocation.withDefaultNamespace("chests/stronghold_library"),
        UNDERWATER_RUIN_BIG = ResourceLocation.withDefaultNamespace("chests/underwater_ruin_big"),
        UNDERWATER_RUIN_SMALL = ResourceLocation.withDefaultNamespace("chests/underwater_ruin_small"),
        WOODLAND_MANSION = ResourceLocation.withDefaultNamespace("chests/woodland_mansion"),
        VILLAGE_ARMORER = ResourceLocation.withDefaultNamespace("chests/village/village_armorer"),
        VILLAGE_BUTCHER = ResourceLocation.withDefaultNamespace("chests/village/village_butcher"),
        VILLAGE_CARTOGRAPHER = ResourceLocation.withDefaultNamespace("chests/village/village_cartographer"),
        VILLAGE_DESERT_HOUSE = ResourceLocation.withDefaultNamespace("chests/village/village_desert_house"),
        VILLAGE_FISHER = ResourceLocation.withDefaultNamespace("chests/village/village_fisher"),
        VILLAGE_FLETCHER = ResourceLocation.withDefaultNamespace("chests/village/village_fletcher"),
        VILLAGE_MASON = ResourceLocation.withDefaultNamespace("chests/village/village_mason"),
        VILLAGE_PLAINS_HOUSE = ResourceLocation.withDefaultNamespace("chests/village/village_plains_house"),
        VILLAGE_SAVANNA_HOUSE = ResourceLocation.withDefaultNamespace("chests/village/village_savanna_house"),
        VILLAGE_TAIGA_HOUSE = ResourceLocation.withDefaultNamespace("chests/village/village_taiga_house"),
        VILLAGE_TANNERY = ResourceLocation.withDefaultNamespace("chests/village/village_tannery"),
        VILLAGE_TEMPLE = ResourceLocation.withDefaultNamespace("chests/village/village_temple"),
        VILLAGE_TOOLSMITH = ResourceLocation.withDefaultNamespace("chests/village/village_toolsmith"),
        VILLAGE_WEAPONSMITH = ResourceLocation.withDefaultNamespace("chests/village/village_weaponsmith"),
        TRIAL_CHAMBERS_CORRIDOR_CHEST = ResourceLocation.withDefaultNamespace("chests/trial_chambers/corridor"),
        TRIAL_CHAMBERS_ENTRANCE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/entrance"),
        TRIAL_CHAMBERS_INTERSECTION = ResourceLocation.withDefaultNamespace("chests/trial_chambers/intersection"),
        TRIAL_CHAMBERS_INTERSECTION_BARREL = ResourceLocation.withDefaultNamespace("chests/trial_chambers/intersection_barrel"),
        TRIAL_CHAMBERS_REWARD = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward"),
        TRIAL_CHAMBERS_REWARD_COMMON = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_common"),
        TRIAL_CHAMBERS_REWARD_OMINOUS = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_ominous"),
        TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_ominous_common"),
        TRIAL_CHAMBERS_REWARD_OMINOUS_RARE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_ominous_rare"),
        TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_ominous_unique"),
        TRIAL_CHAMBERS_REWARD_RARE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_rare"),
        TRIAL_CHAMBERS_REWARD_UNIQUE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_unique"),
        TRIAL_CHAMBERS_SUPPLY = ResourceLocation.withDefaultNamespace("chests/trial_chambers/supply"),

        // endregion

        // region Dispenser Loot Tables

        TRIAL_CHAMBERS_CHAMBER = ResourceLocation.withDefaultNamespace("dispensers/trial_chambers/chamber"),
        TRIAL_CHAMBERS_CORRIDOR_DISPENSER = ResourceLocation.withDefaultNamespace("dispensers/trial_chambers/corridor"),
        TRIAL_CHAMBERS_WATER = ResourceLocation.withDefaultNamespace("dispensers/trial_chambers/water"),

        // endregion

        // region Entity Loot Tables

        BLACK_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/black"),
        BLUE_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/blue"),
        BROWN_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/brown"),
        CYAN_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/cyan"),
        GRAY_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/gray"),
        GREEN_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/green"),
        LIGHT_BLUE_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/light_blue"),
        LIGHT_GRAY_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/light_gray"),
        LIME_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/lime"),
        MAGENTA_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/magenta"),
        ORANGE_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/orange"),
        PINK_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/pink"),
        PURPLE_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/purple"),
        RED_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/red"),
        WHITE_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/white"),
        YELLOW_SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep/yellow"),
        ALLAY = ResourceLocation.withDefaultNamespace("entities/allay"),
        ARMADILLO = ResourceLocation.withDefaultNamespace("entities/armadillo"),
        ARMOR_STAND = ResourceLocation.withDefaultNamespace("entities/armor_stand"),
        AXOLOTL = ResourceLocation.withDefaultNamespace("entities/axolotl"),
        BAT = ResourceLocation.withDefaultNamespace("entities/bat"),
        BEE = ResourceLocation.withDefaultNamespace("entities/bee"),
        BLAZE = ResourceLocation.withDefaultNamespace("entities/blaze"),
        BOGGED = ResourceLocation.withDefaultNamespace("entities/bogged"),
        BREEZE = ResourceLocation.withDefaultNamespace("entities/breeze"),
        CAMEL = ResourceLocation.withDefaultNamespace("entities/camel"),
        CAT = ResourceLocation.withDefaultNamespace("entities/cat"),
        CAVE_SPIDER = ResourceLocation.withDefaultNamespace("entities/cave_spider"),
        CHICKEN = ResourceLocation.withDefaultNamespace("entities/chicken"),
        COD = ResourceLocation.withDefaultNamespace("entities/cod"),
        COW = ResourceLocation.withDefaultNamespace("entities/cow"),
        CREEPER = ResourceLocation.withDefaultNamespace("entities/creeper"),
        DOLPHIN = ResourceLocation.withDefaultNamespace("entities/dolphin"),
        DONKEY = ResourceLocation.withDefaultNamespace("entities/donkey"),
        DROWNED = ResourceLocation.withDefaultNamespace("entities/drowned"),
        ELDER_GUARDIAN = ResourceLocation.withDefaultNamespace("entities/elder_guardian"),
        ENDER_DRAGON = ResourceLocation.withDefaultNamespace("entities/ender_dragon"),
        ENDERMAN = ResourceLocation.withDefaultNamespace("entities/enderman"),
        ENDERMITE = ResourceLocation.withDefaultNamespace("entities/endermite"),
        EVOKER = ResourceLocation.withDefaultNamespace("entities/evoker"),
        FOX = ResourceLocation.withDefaultNamespace("entities/fox"),
        FROG = ResourceLocation.withDefaultNamespace("entities/frog"),
        GHAST = ResourceLocation.withDefaultNamespace("entities/ghast"),
        GIANT = ResourceLocation.withDefaultNamespace("entities/giant"),
        GLOW_SQUID = ResourceLocation.withDefaultNamespace("entities/glow_squid"),
        GOAT = ResourceLocation.withDefaultNamespace("entities/goat"),
        GUARDIAN = ResourceLocation.withDefaultNamespace("entities/guardian"),
        HOGLIN = ResourceLocation.withDefaultNamespace("entities/hoglin"),
        HORSE = ResourceLocation.withDefaultNamespace("entities/horse"),
        HUSK = ResourceLocation.withDefaultNamespace("entities/husk"),
        ILLUSIONER = ResourceLocation.withDefaultNamespace("entities/illusioner"),
        IRON_GOLEM = ResourceLocation.withDefaultNamespace("entities/iron_golem"),
        LLAMA = ResourceLocation.withDefaultNamespace("entities/llama"),
        MAGMA_CUBE = ResourceLocation.withDefaultNamespace("entities/magma_cube"),
        MOOSHROOM = ResourceLocation.withDefaultNamespace("entities/mooshroom"),
        MULE = ResourceLocation.withDefaultNamespace("entities/mule"),
        OCELOT = ResourceLocation.withDefaultNamespace("entities/ocelot"),
        PANDA = ResourceLocation.withDefaultNamespace("entities/panda"),
        PARROT = ResourceLocation.withDefaultNamespace("entities/parrot"),
        PHANTOM = ResourceLocation.withDefaultNamespace("entities/phantom"),
        PIG = ResourceLocation.withDefaultNamespace("entities/pig"),
        PIGLIN = ResourceLocation.withDefaultNamespace("entities/piglin"),
        PIGLIN_BRUTE = ResourceLocation.withDefaultNamespace("entities/piglin_brute"),
        PILLAGER = ResourceLocation.withDefaultNamespace("entities/pillager"),
        PLAYER = ResourceLocation.withDefaultNamespace("entities/player"),
        POLAR_BEAR = ResourceLocation.withDefaultNamespace("entities/polar_bear"),
        PUFFERFISH = ResourceLocation.withDefaultNamespace("entities/pufferfish"),
        RABBIT = ResourceLocation.withDefaultNamespace("entities/rabbit"),
        RAVAGER = ResourceLocation.withDefaultNamespace("entities/ravager"),
        SALMON = ResourceLocation.withDefaultNamespace("entities/salmon"),
        SHEEP = ResourceLocation.withDefaultNamespace("entities/sheep"),
        SHULKER = ResourceLocation.withDefaultNamespace("entities/shulker"),
        SILVERFISH = ResourceLocation.withDefaultNamespace("entities/silverfish"),
        SKELETON = ResourceLocation.withDefaultNamespace("entities/skeleton"),
        SKELETON_HORSE = ResourceLocation.withDefaultNamespace("entities/skeleton_horse"),
        SLIME = ResourceLocation.withDefaultNamespace("entities/slime"),
        SNIFFER = ResourceLocation.withDefaultNamespace("entities/sniffer"),
        SNOW_GOLEM = ResourceLocation.withDefaultNamespace("entities/snow_golem"),
        SPIDER = ResourceLocation.withDefaultNamespace("entities/spider"),
        SQUID = ResourceLocation.withDefaultNamespace("entities/squid"),
        STRAY = ResourceLocation.withDefaultNamespace("entities/stray"),
        STRIDER = ResourceLocation.withDefaultNamespace("entities/strider"),
        TADPOLE = ResourceLocation.withDefaultNamespace("entities/tadpole"),
        TRADER_LLAMA = ResourceLocation.withDefaultNamespace("entities/trader_llama"),
        TROPICAL_FISH = ResourceLocation.withDefaultNamespace("entities/tropical_fish"),
        TURTLE = ResourceLocation.withDefaultNamespace("entities/turtle"),
        VEX = ResourceLocation.withDefaultNamespace("entities/vex"),
        VILLAGER = ResourceLocation.withDefaultNamespace("entities/villager"),
        VINDICATOR = ResourceLocation.withDefaultNamespace("entities/vindicator"),
        WANDERING_TRADER = ResourceLocation.withDefaultNamespace("entities/wandering_trader"),
        WARDEN = ResourceLocation.withDefaultNamespace("entities/warden"),
        WITCH = ResourceLocation.withDefaultNamespace("entities/witch"),
        WITHER = ResourceLocation.withDefaultNamespace("entities/wither"),
        WITHER_SKELETON = ResourceLocation.withDefaultNamespace("entities/wither_skeleton"),
        WOLF = ResourceLocation.withDefaultNamespace("entities/wolf"),
        ZOGLIN = ResourceLocation.withDefaultNamespace("entities/zoglin"),
        ZOMBIE = ResourceLocation.withDefaultNamespace("entities/zombie"),
        ZOMBIE_HORSE = ResourceLocation.withDefaultNamespace("entities/zombie_horse"),
        ZOMBIE_VILLAGER = ResourceLocation.withDefaultNamespace("entities/zombie_villager"),
        ZOMBIFIED_PIGLIN = ResourceLocation.withDefaultNamespace("entities/zombified_piglin"),

        // endregion

        // region Equipment Loot Tables

        EQUIPMENT_TRIAL_CHAMBER = ResourceLocation.withDefaultNamespace("equipment/trial_chamber"),
        EQUIPMENT_TRIAL_CHAMBER_MELEE = ResourceLocation.withDefaultNamespace("equipment/trial_chamber_melee"),
        EQUIPMENT_TRIAL_CHAMBER_RANGED = ResourceLocation.withDefaultNamespace("equipment/trial_chamber_ranged"),

        // endregion

        // region Gameplay Loot Tables

        CAT_MORNING_GIFT = ResourceLocation.withDefaultNamespace("gameplay/cat_morning_gift"),
        FISHING = ResourceLocation.withDefaultNamespace("gameplay/fishing"),
        PANDA_SNEEZE = ResourceLocation.withDefaultNamespace("gameplay/panda_sneeze"),
        PIGLIN_BARTERING = ResourceLocation.withDefaultNamespace("gameplay/piglin_bartering"),
        SNIFFER_DIGGING = ResourceLocation.withDefaultNamespace("gameplay/sniffer_digging"),
        FISHING_FISH = ResourceLocation.withDefaultNamespace("gameplay/fishing/fish"),
        FISHING_JUNK = ResourceLocation.withDefaultNamespace("gameplay/fishing/junk"),
        FISHING_TREASURE = ResourceLocation.withDefaultNamespace("gameplay/fishing/treasure"),
        HERO_OF_THE_VILLAGE_GIFT_ARMORER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/armorer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_BUTCHER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/butcher_gift"),
        HERO_OF_THE_VILLAGE_GIFT_CARTOGRAPHER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/cartographer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_CLERIC = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/cleric_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FARMER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/farmer_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FISHERMAN = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/fisherman_gift"),
        HERO_OF_THE_VILLAGE_GIFT_FLETCHER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/fletcher_gift"),
        HERO_OF_THE_VILLAGE_GIFT_LEATHERWORKER = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/leatherworker_gift"),
        HERO_OF_THE_VILLAGE_GIFT_LIBRARIAN = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/librarian_gift"),
        HERO_OF_THE_VILLAGE_GIFT_MASON = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/mason_gift"),
        HERO_OF_THE_VILLAGE_GIFT_SHEPHERD = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/shepherd_gift"),
        HERO_OF_THE_VILLAGE_GIFT_TOOLSMITH = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/toolsmith_gift"),
        HERO_OF_THE_VILLAGE_GIFT_WEAPONSMITH = ResourceLocation.withDefaultNamespace("gameplay/hero_of_the_village/weaponsmith_gift"),

        // endregion

        // region Pot Loot Tables

        TRIAL_CHAMBERS_CORRIDOR_POT = ResourceLocation.withDefaultNamespace("pots/trial_chambers/corridor"),

        // endregion

        // region Shearing Loot Tables

        SHEARING_BOGGED = ResourceLocation.withDefaultNamespace("shearing/bogged"),

        // endregion

        // region Spawner Loot Tables

        TRIAL_CHAMBERS_CONSUMABLES = ResourceLocation.withDefaultNamespace("spawners/trial_chamber/consumables"),
        TRIAL_CHAMBERS_KEY = ResourceLocation.withDefaultNamespace("spawners/trial_chamber/key")

        // endregion
    ;

    // endregion
}