package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("all")
public class ChrysalisTags {

    /**
     * Item Tags
     **/

    public static final TagKey<Item>

        // region Chrysalis Items

        DEBUG_UTILITY_ITEMS = registerItemTag("debug_utility_items"),

        // endregion

        // region Common Items

        BANNER_PATTERNS = registerItemTag("banner_patterns"),
        DISC_FRAGMENTS = registerItemTag("disc_fragments"),
        FILLED_BOTTLES = registerItemTag("filled_bottles"),
        FILLED_BOWLS = registerItemTag("filled_bowls"),
        FISH_BUCKETS = registerItemTag("fish_buckets"),
        INSTRUMENTS = registerItemTag("instruments"),
        TELEPORTING_FOODS = registerItemTag("teleporting_foods"),

        // endregion

        // region Block Items

        AMETHYST_BLOCK_ITEMS = registerItemTag("amethyst_blocks"),
        BRUSHABLE_BLOCK_ITEMS = registerItemTag("brushable_blocks"),
        CAMPFIRE_ITEMS = registerItemTag("campfires"),
        CONTAINER_ITEMS = registerItemTag("containers"),
        COPPER_BLOCK_ITEMS = registerItemTag("copper_blocks"),
        COPPER_GRATE_ITEMS = registerItemTag("copper_grates"),
        LANTERN_ITEMS = registerItemTag("lanterns"),
        PRESSURE_PLATE_ITEMS = registerItemTag("pressure_plates"),
        PRISMARINE_BLOCK_ITEMS = registerItemTag("prismarine_blocks"),
        PURPUR_ITEMS = registerItemTag("purpur"),
        SOUL_SAND_ITEMS = registerItemTag("soul_sand"),
        STONE_PRESSURE_PLATE_ITEMS = registerItemTag("stone_pressure_plates"),
        TORCH_ITEMS = registerItemTag("torches"),

        // endregion

        // region Interactable Items

        CHARGES_RESPAWN_ANCHORS = registerItemTag("charges_respawn_anchors"),
        DUPLICATES_ALLAYS = registerItemTag("duplicates_allays"),
        PIGLIN_BARTERING_ITEMS = registerItemTag("piglin_bartering_items"),
        REPAIRS_IRON_GOLEMS = registerItemTag("repairs_iron_golems"),
        TNT_IGNITERS = registerItemTag("tnt_igniters"),

        // endregion

        // region Equipable Items

        HORSE_ARMOR = registerItemTag("horse_armor"),
        PIGLIN_PACIFYING_ARMOR = registerItemTag("piglin_pacifying_armor"),
        POWDER_SNOW_WALKABLE_ITEMS = registerItemTag("powder_snow_walkable_items"),
        PROTECTS_AGAINST_ENDERMEN = registerItemTag("protects_against_endermen"),

        // endregion

        // region Item Entities

        IMMUNE_TO_DAMAGE = registerItemTag("immune_to_damage"),
        IMMUNE_TO_DESPAWNING = registerItemTag("immune_to_despawning"),
        IMMUNE_TO_EXPLOSIONS = registerItemTag("immune_to_explosions"),
        IMMUNE_TO_FIRE = registerItemTag("immune_to_fire"),
        INCREASED_DESPAWN_TIME = registerItemTag("increased_despawn_time")

        // endregion
    ;

    /**
     * Block Tags
     **/

    public static final TagKey<Block>

        // region Common Blocks

        AMETHYST_BLOCKS = registerBlockTag("amethyst_blocks"),
        BRUSHABLE_BLOCKS = registerBlockTag("brushable_blocks"),
        CONTAINERS = registerBlockTag("containers"),
        COPPER_BLOCKS = registerBlockTag("copper_blocks"),
        COPPER_GRATES = registerBlockTag("copper_grates"),
        LANTERNS = registerBlockTag("lanterns"),
        PRISMARINE_BLOCKS = registerBlockTag("prismarine_blocks"),
        PURPUR = registerBlockTag("purpur"),
        SNIFFER_PLANT_CROPS = registerBlockTag("sniffer_plant_crops"),
        SOUL_SAND = registerBlockTag("soul_sand"),
        TORCHES = registerBlockTag("torches"),
        UNBREAKABLE_BLOCKS = registerBlockTag("unbreakable_blocks"),
        WAXABLE_BLOCKS = registerBlockTag("waxable_blocks"),
        WAXED_BLOCKS = registerBlockTag("waxed_blocks"),

        // endregion

        // region Technical

        ALLOWS_BEACON_BEAM_PASSTHROUGH = registerBlockTag("allows_beacon_beam_passthrough"),
        ALLOWS_USE_WHILE_SNEAKING = registerBlockTag("allows_use_while_sneaking"),
        BASE_STONE_END = registerBlockTag("base_stone_end"),
        CHORUS_PLANT_CAN_GROW_ON = registerBlockTag("chorus_plant_can_grow_on"),
        END_CRYSTAL_BASE_BLOCKS = registerBlockTag("end_crystal_base_blocks"),
        NETHER_PORTAL_BASE_BLOCKS = registerBlockTag("nether_portal_base_blocks"),
        NETHER_WART_CAN_GROW_ON = registerBlockTag("nether_wart_can_grow_on"),
        TURTLE_EGGS_CAN_HATCH_ON = registerBlockTag("turtle_eggs_can_hatch_on"),

        // endregion

        // region Mineable Block Tags

        MINEABLE_WITH_SWORD = registerBlockTag("mineable/sword"),
        MINEABLE_WITH_SHEARS = registerBlockTag("mineable/shears")

        // endregion
    ;

    /**
     * Entity Tags
     **/

    public static final TagKey<EntityType<?>>

        // region Living Entities

        ENDER = registerEntityTag("ender"),
        GOLEM = registerEntityTag("golem"),
        PIGLIN = registerEntityTag("piglin"),
        SLIME = registerEntityTag("slime"),
        VILLAGER = registerEntityTag("villager"),

        // endregion

        // region Non-Living Entities

        ATTACKABLE_STATIC_ENTITIES = registerEntityTag("attackable_static_entities"),
        VEHICLE = registerEntityTag("vehicle"),

        // endregion

        // region Technical

        CAN_SPAWN_ON_LEAVES = registerEntityTag("can_spawn_on_leaves"),
        CANNOT_RIDE_BOATS = registerEntityTag("cannot_ride_boats")

        // endregion
    ;

    /**
     * Damage Type Tags
     **/

    public static final TagKey<DamageType>
        IS_MAGIC = registerDamageTypeTag("is_magic"),
        IS_RANGED = registerDamageTypeTag("is_ranged")
    ;

    /**
     * Biome Tags
     **/

    public static final TagKey<Biome>
        WITHOUT_MOB_SPAWNS = registerBiomeTag("without_mob_spawns")
    ;

    /**
     * Registries
     **/

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

    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, Chrysalis.id(name));
    }

    // endregion
}