package net.junebug.chrysalis.common.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.junebug.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class CTags {

    /**
     * The registry for tags added by chrysalis.
     **/

    // region Item Tags

    public static final TagKey<Item>
        BANNER_PATTERNS = registerItemTag("banner_patterns"),
        BAR_ITEMS = registerItemTag("bars"),
        CAMPFIRES = registerItemTag("campfires"),
        CANNOT_GIVE_WITH_COMMANDS = registerItemTag("cannot_give_with_commands"),
        CHARGES_RESPAWN_ANCHORS = registerItemTag("charges_respawn_anchors"),
        CONTAINER_ITEMS = registerItemTag("containers"),
        COPPER_BLOCK_ITEMS = registerItemTag("copper_blocks"),
        COPPER_GRATE_ITEMS = registerItemTag("copper_grates"),
        DEBUG_UTILITIES = registerItemTag("debug_utilities"),
        DISC_FRAGMENTS = registerItemTag("disc_fragments"),
        DOLPHIN_FOODS = registerItemTag("dolphin_foods"),
        END_ORE_ITEMS = registerItemTag("end_ores"),
        EVENT_TEST_ITEMS = registerItemTag("event_test_items"),
        FILLED_BOTTLES = registerItemTag("filled_bottles"),
        FILLED_BOWLS = registerItemTag("filled_bowls"),
        FISH_BUCKETS = registerItemTag("fish_buckets"),
        GIVES_ARTHROPOD_SIGHT = registerItemTag("gives_arthropod_sight"),
        GIVES_BLIND_SIGHT = registerItemTag("gives_blind_sight"),
        GIVES_CREEPER_SIGHT = registerItemTag("gives_creeper_sight"),
        GIVES_ENDER_SIGHT = registerItemTag("gives_ender_sight"),
        GIVES_RESIN_SIGHT = registerItemTag("gives_resin_sight"),
        GIVES_SKELETAL_SIGHT = registerItemTag("gives_skeletal_sight"),
        GIVES_ZOMBIE_SIGHT = registerItemTag("gives_zombie_sight"),
        GRATE_ITEMS = registerItemTag("grates"),
        HIDES_NAME_TAGS = registerItemTag("hides_name_tags"),
        HORSE_ARMOR = registerItemTag("horse_armor"),
        INFESTED_BLOCK_ITEMS = registerItemTag("infested_blocks"),
        LANTERN_ITEMS = registerItemTag("lanterns"),
        NETHER_ORE_ITEMS = registerItemTag("nether_ores"),
        OPEN_FLOWER_ITEMS = registerItemTag("open_flowers"),
        OVERWORLD_ORE_ITEMS = registerItemTag("overworld_ores"),
        PIGLIN_BARTERING_ITEMS = registerItemTag("piglin_bartering_items"),
        PORTALS = registerItemTag("portals"),
        POWDER_SNOW_WALKABLE_ITEMS = registerItemTag("powder_snow_walkable_items"),
        PRESSURE_PLATE_ITEMS = registerItemTag("pressure_plates"),
        PRISMARINE_BLOCK_ITEMS = registerItemTag("prismarine_blocks"),
        PURPUR_BLOCK_ITEMS = registerItemTag("purpur_blocks"),
        REPAIRS_IRON_GOLEMS = registerItemTag("repairs_iron_golems"),
        SOUL_SAND_ITEMS = registerItemTag("soul_sands"),
        STONE_PRESSURE_PLATE_ITEMS = registerItemTag("stone_pressure_plates"),
        SUSPICIOUS_BLOCK_ITEMS = registerItemTag("suspicious_blocks"),
        TELEPORT_TO_DIMENSION_ITEMS = registerItemTag("teleport_to_dimension_items"),
        TNT_IGNITERS = registerItemTag("tnt_igniters"),
        TORCH_ITEMS = registerItemTag("torches"),
        WAXED_BLOCK_ITEMS = registerItemTag("waxed_blocks"),
        ENCHANTABLE_ANIMAL_ARMOR = registerItemTag("enchantable/animal_armor"),
        ENCHANTABLE_AXE = registerItemTag("enchantable/axe"),
        ENCHANTABLE_BRUSH = registerItemTag("enchantable/brush"),
        ENCHANTABLE_BUNDLE = registerItemTag("enchantable/bundle"),
        ENCHANTABLE_COMPASS = registerItemTag("enchantable/compass"),
        ENCHANTABLE_ELYTRA = registerItemTag("enchantable/elytra"),
        ENCHANTABLE_FLINT_AND_STEEL = registerItemTag("enchantable/flint_and_steel"),
        ENCHANTABLE_FOOD_ON_A_STICK = registerItemTag("enchantable/food_on_a_stick"),
        ENCHANTABLE_HOE = registerItemTag("enchantable/hoe"),
        ENCHANTABLE_HORSE_ARMOR = registerItemTag("enchantable/horse_armor"),
        ENCHANTABLE_PICKAXE = registerItemTag("enchantable/pickaxe"),
        ENCHANTABLE_SHEARS = registerItemTag("enchantable/shears"),
        ENCHANTABLE_SHIELD = registerItemTag("enchantable/shield"),
        ENCHANTABLE_SHOVEL = registerItemTag("enchantable/shovel"),
        ENCHANTABLE_SHULKER_BOX = registerItemTag("enchantable/shulker_box"),
        ENCHANTABLE_SPYGLASS = registerItemTag("enchantable/spyglass"),
        ENCHANTABLE_STORAGE = registerItemTag("enchantable/storage"),
        ENCHANTABLE_WOLF_ARMOR = registerItemTag("enchantable/wolf_armor")
    ;

    // endregion

    // region Block Tags

    public static final TagKey<Block>
        MINEABLE_WITH_SHEARS = registerBlockTag("mineable/shears"),
        MINEABLE_WITH_SHEARS_FAST = registerBlockTag("mineable/shears_fast"),
        MINEABLE_WITH_SHEARS_NORMAL = registerBlockTag("mineable/shears_normal"),
        MINEABLE_WITH_SHEARS_SLOW = registerBlockTag("mineable/shears_slow"),
        MINEABLE_WITH_SWORDS = registerBlockTag("mineable/sword"),
        MINEABLE_WITH_SWORDS_DOES_NOT_DROP_BLOCK = registerBlockTag("mineable/sword_does_not_drop_block"),
        MINEABLE_WITH_SWORDS_DROPS_BLOCK = registerBlockTag("mineable/sword_drops_block"),
        ALLOWS_BEACON_BEAM_PASSTHROUGH = registerBlockTag("allows_beacon_beam_passthrough"),
        ALLOWS_PLACEMENT_WITH_BUILDING_FATIGUE = registerBlockTag("allows_placement_with_building_fatigue"),
        ALLOWS_USE_WHILE_SNEAKING = registerBlockTag("allows_use_while_sneaking"),
        BARS = registerBlockTag("bars"),
        BASE_STONE_END = registerBlockTag("base_stone_end"),
        CHORUS_PLANTS_CAN_GROW_ON = registerBlockTag("chorus_plants_can_grow_on"),
        CONTAINERS = registerBlockTag("containers"),
        COPPER_BLOCKS = registerBlockTag("copper_blocks"),
        COPPER_GRATES = registerBlockTag("copper_grates"),
        DAMPENS_BLOCK_AMBIENT_SOUNDS = registerBlockTag("dampens_block_ambient_sounds"),
        EARTHQUAKE_BREAKABLE = registerBlockTag("earthquake_breakable"),
        EARTHQUAKE_COLLISIONS_IGNORED = registerBlockTag("earthquake_collisions_ignored"),
        END_CRYSTAL_BASE_BLOCKS = registerBlockTag("end_crystal_base_blocks"),
        END_ORES = registerBlockTag("end_ores"),
        GRATES = registerBlockTag("grates"),
        INFESTED_BLOCKS = registerBlockTag("infested_blocks"),
        INHERENTLY_WATERLOGGED = registerBlockTag("inherently_waterlogged"),
        LANTERNS = registerBlockTag("lanterns"),
        LASER_BLASTS_RICOCHET_FROM = registerBlockTag("laser_blasts_ricochet_from"),
        NETHER_ORES = registerBlockTag("nether_ores"),
        NETHER_WARTS_CAN_GROW_ON = registerBlockTag("nether_warts_can_grow_on"),
        OPEN_FLOWERS = registerBlockTag("open_flowers"),
        OVERWORLD_ORES = registerBlockTag("overworld_ores"),
        PRISMARINE_BLOCKS = registerBlockTag("prismarine_blocks"),
        PURPUR_BLOCKS = registerBlockTag("purpur_blocks"),
        SNIFFER_PLANT_CROPS = registerBlockTag("sniffer_plant_crops"),
        SOUL_SANDS = registerBlockTag("soul_sands"),
        SPEEDS_UP_ZOMBIE_VILLAGER_CURING = registerBlockTag("speeds_up_zombie_villager_curing"),
        SUSPICIOUS_BLOCKS = registerBlockTag("suspicious_blocks"),
        TORCHES = registerBlockTag("torches"),
        TURTLE_EGGS_CAN_HATCH_ON = registerBlockTag("turtle_eggs_can_hatch_on"),
        UNBREAKABLE_BLOCKS = registerBlockTag("unbreakable_blocks"),
        WAXABLE_BLOCKS = registerBlockTag("waxable_blocks"),
        WAXED_BLOCKS = registerBlockTag("waxed_blocks")
    ;

    // endregion

    // region Entity Type Tags

    public static final TagKey<EntityType<?>>
        ALWAYS_PLAYS_ENCOUNTER_MUSIC = registerEntityTag("always_plays_encounter_music"),
        CAN_SPAWN_ON_LEAVES = registerEntityTag("can_spawn_on_leaves"),
        CANNOT_RIDE_BOATS = registerEntityTag("cannot_ride_boats"),
        COPYING_SPAWN_EGG_BLACKLISTED = registerEntityTag("copying_spawn_egg_blacklisted"),
        DISPLAYS_KILL_WAND_CROSSHAIR = registerEntityTag("displays_kill_wand_crosshair"),
        DOES_NOT_FLIP_OVER_UPON_DEATH = registerEntityTag("does_not_flip_over_upon_death"),
        ENDERS = registerEntityTag("enders"),
        FLIPS_OVER_UPON_DEATH = registerEntityTag("flips_over_upon_death"),
        GOLEMS = registerEntityTag("golems"),
        HAS_ARTHROPOD_SIGHT = registerEntityTag("has_arthropod_sight"),
        HAS_BLIND_SIGHT = registerEntityTag("has_blind_sight"),
        HAS_CREEPER_SIGHT = registerEntityTag("has_creeper_sight"),
        HAS_ENDER_SIGHT = registerEntityTag("has_ender_sight"),
        HAS_RESIN_SIGHT = registerEntityTag("has_resin_sight"),
        HAS_SKELETAL_SIGHT = registerEntityTag("has_skeletal_sight"),
        HAS_ZOMBIE_SIGHT = registerEntityTag("has_zombie_sight"),
        HIDDEN_FROM_STATISTICS_MENU = registerEntityTag("hidden_from_statistics_menu"),
        HIDDEN_FROM_SUMMON_COMMAND = registerEntityTag("hidden_from_summon_command"),
        IMMUNE_TO_EARTHQUAKES = registerEntityTag("immune_to_earthquakes"),
        NON_LIVING_ATTACKABLES = registerEntityTag("non_living_attackables"),
        PIGLIN_AVOIDED = registerEntityTag("piglin_avoided"),
        PIGLINS = registerEntityTag("piglins"),
        SLIMES = registerEntityTag("slimes"),
        VEHICLES = registerEntityTag("vehicles"),
        VILLAGERS = registerEntityTag("villagers"),
        WARDEN_IGNORED = registerEntityTag("warden_ignored")
    ;

    // endregion

    // region Damage Type Tags

    public static final TagKey<DamageType>
        BYPASSES_DAMAGE_CAPACITY = registerDamageTypeTag("bypasses_damage_capacity"),
        DOES_NOT_EXPLODE_END_CRYSTALS = registerDamageTypeTag("does_not_explode_end_crystals"),
        IS_MAGIC = registerDamageTypeTag("is_magic"),
        IS_RANGED = registerDamageTypeTag("is_ranged"),
        PREVENTS_MOB_SPLITTING = registerDamageTypeTag("prevents_mob_splitting")
    ;

    // endregion

    // region Biome Tags

    public static final TagKey<Biome>
        WITHOUT_MOB_SPAWNS = registerBiomeTag("without_mob_spawns")
    ;

    // endregion

    // region Registry

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, Chrysalis.resourceLocationId(name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Chrysalis.resourceLocationId(name));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Chrysalis.resourceLocationId(name));
    }

    private static TagKey<DamageType> registerDamageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Chrysalis.resourceLocationId(name));
    }

    @SuppressWarnings("all")
    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, Chrysalis.resourceLocationId(name));
    }

    // endregion
}