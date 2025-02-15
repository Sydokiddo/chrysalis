package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class ChrysalisTags {

    // region Item Tags

    public static final TagKey<Item>
        BANNER_PATTERNS = registerItemTag("banner_patterns"),
        CAMPFIRES = registerItemTag("campfires"),
        CHARGES_RESPAWN_ANCHORS = registerItemTag("charges_respawn_anchors"),
        CONTAINER_ITEMS = registerItemTag("containers"),
        COPPER_BLOCK_ITEMS = registerItemTag("copper_blocks"),
        COPPER_GRATE_ITEMS = registerItemTag("copper_grates"),
        DEBUG_UTILITIES = registerItemTag("debug_utilities"),
        DISC_FRAGMENTS = registerItemTag("disc_fragments"),
        DOLPHIN_FOODS = registerItemTag("dolphin_foods"),
        END_ORE_ITEMS = registerItemTag("end_ores"),
        FILLED_BOTTLES = registerItemTag("filled_bottles"),
        FILLED_BOWLS = registerItemTag("filled_bowls"),
        FISH_BUCKETS = registerItemTag("fish_buckets"),
        HORSE_ARMOR = registerItemTag("horse_armor"),
        IMMUNE_TO_DAMAGE = registerItemTag("immune_to_damage"),
        IMMUNE_TO_DESPAWNING = registerItemTag("immune_to_despawning"),
        INCREASED_DESPAWN_TIME = registerItemTag("increased_despawn_time"),
        INCREASED_PICK_RADIUS = registerItemTag("increased_pick_radius"),
        LANTERN_ITEMS = registerItemTag("lanterns"),
        NETHER_ORE_ITEMS = registerItemTag("nether_ores"),
        OPEN_FLOWER_ITEMS = registerItemTag("open_flowers"),
        OVERWORLD_ORE_ITEMS = registerItemTag("overworld_ores"),
        PIGLIN_BARTERING_ITEMS = registerItemTag("piglin_bartering_items"),
        POWDER_SNOW_WALKABLE_ITEMS = registerItemTag("powder_snow_walkable_items"),
        PRESSURE_PLATE_ITEMS = registerItemTag("pressure_plates"),
        PRISMARINE_BLOCK_ITEMS = registerItemTag("prismarine_blocks"),
        PURPUR_BLOCK_ITEMS = registerItemTag("purpur_blocks"),
        REPAIRS_IRON_GOLEMS = registerItemTag("repairs_iron_golems"),
        SOUL_SAND_ITEMS = registerItemTag("soul_sands"),
        STONE_PRESSURE_PLATE_ITEMS = registerItemTag("stone_pressure_plates"),
        TNT_IGNITERS = registerItemTag("tnt_igniters"),
        TORCH_ITEMS = registerItemTag("torches"),
        WAXED_BLOCK_ITEMS = registerItemTag("waxed_blocks")
    ;

    // endregion

    // region Block Tags

    public static final TagKey<Block>
        MINEABLE_WITH_SHEARS = registerBlockTag("mineable/shears"),
        MINEABLE_WITH_SWORDS = registerBlockTag("mineable/sword"),
        ALLOWS_BEACON_BEAM_PASSTHROUGH = registerBlockTag("allows_beacon_beam_passthrough"),
        ALLOWS_USE_WHILE_SNEAKING = registerBlockTag("allows_use_while_sneaking"),
        BASE_STONE_END = registerBlockTag("base_stone_end"),
        CHORUS_PLANTS_CAN_GROW_ON = registerBlockTag("chorus_plants_can_grow_on"),
        CONTAINERS = registerBlockTag("containers"),
        COPPER_BLOCKS = registerBlockTag("copper_blocks"),
        COPPER_GRATES = registerBlockTag("copper_grates"),
        END_CRYSTAL_BASE_BLOCKS = registerBlockTag("end_crystal_base_blocks"),
        END_ORES = registerBlockTag("end_ores"),
        LANTERNS = registerBlockTag("lanterns"),
        NETHER_ORES = registerBlockTag("nether_ores"),
        NETHER_PORTAL_BASE_BLOCKS = registerBlockTag("nether_portal_base_blocks"),
        NETHER_WARTS_CAN_GROW_ON = registerBlockTag("nether_warts_can_grow_on"),
        OPEN_FLOWERS = registerBlockTag("open_flowers"),
        OVERWORLD_ORES = registerBlockTag("overworld_ores"),
        PRISMARINE_BLOCKS = registerBlockTag("prismarine_blocks"),
        PURPUR_BLOCKS = registerBlockTag("purpur_blocks"),
        SNIFFER_PLANT_CROPS = registerBlockTag("sniffer_plant_crops"),
        SOUL_SANDS = registerBlockTag("soul_sands"),
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
        ENDERS = registerEntityTag("enders"),
        GOLEMS = registerEntityTag("golems"),
        HAS_ARTHROPOD_SIGHT = registerEntityTag("has_arthropod_sight"),
        HAS_CREEPER_SIGHT = registerEntityTag("has_creeper_sight"),
        HAS_ENDER_SIGHT = registerEntityTag("has_ender_sight"),
        NON_LIVING_ATTACKABLES = registerEntityTag("non_living_attackables"),
        PIGLIN_AVOIDED = registerEntityTag("piglin_avoided"),
        PIGLINS = registerEntityTag("piglins"),
        SLIMES = registerEntityTag("slimes"),
        STATISTICS_MENU_IGNORED = registerEntityTag("statistics_menu_ignored"),
        VEHICLES = registerEntityTag("vehicles"),
        VILLAGERS = registerEntityTag("villagers"),
        WARDEN_IGNORED = registerEntityTag("warden_ignored")
    ;

    // endregion

    // region Damage Type Tags

    public static final TagKey<DamageType>
        IS_MAGIC = registerDamageTypeTag("is_magic"),
        IS_RANGED = registerDamageTypeTag("is_ranged")
    ;

    // endregion

    // region Biome Tags

    public static final TagKey<Biome>
        WITHOUT_MOB_SPAWNS = registerBiomeTag("without_mob_spawns")
    ;

    // endregion

    // region Registry

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, Chrysalis.id(name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Chrysalis.id(name));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Chrysalis.id(name));
    }

    private static TagKey<DamageType> registerDamageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Chrysalis.id(name));
    }

    @SuppressWarnings("all")
    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, Chrysalis.id(name));
    }

    // endregion
}