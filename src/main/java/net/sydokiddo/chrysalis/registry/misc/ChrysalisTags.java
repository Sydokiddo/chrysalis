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

        // region Common Items

        DISC_FRAGMENTS = registerItemTag("disc_fragments"),
        INSTRUMENTS = registerItemTag("instruments"),
        SEEDS = registerItemTag("seeds"),
        TELEPORTING_FOODS = registerItemTag("teleporting_foods"),
        MEATS = registerItemTag("meats"),
        FILLED_BOTTLES = registerItemTag("filled_bottles"),
        FILLED_BUCKETS = registerItemTag("filled_buckets"),
        FISH_BUCKETS = registerItemTag("fish_buckets"),
        FILLED_BOWLS = registerItemTag("filled_bowls"),
        BRICKS = registerItemTag("bricks"),
        BANNER_PATTERNS = registerItemTag("banner_patterns"),

        // endregion

        // region Block Items

        AMETHYST_BLOCK_ITEMS = registerItemTag("amethyst_blocks"),
        SOUL_SAND_ITEMS = registerItemTag("soul_sand"),
        CONCRETE_ITEMS = registerItemTag("concrete"),
        CONCRETE_POWDER_ITEMS = registerItemTag("concrete_powder"),
        GLAZED_TERRACOTTA_ITEMS = registerItemTag("glazed_terracotta"),
        COPPER_BLOCK_ITEMS = registerItemTag("copper_blocks"),
        PRISMARINE_BLOCK_ITEMS = registerItemTag("prismarine_blocks"),
        OBSIDIAN_ITEMS = registerItemTag("obsidian"),
        PURPUR_ITEMS = registerItemTag("purpur"),
        HEAD_ITEMS = registerItemTag("heads"),
        CONTAINER_ITEMS = registerItemTag("containers"),
        FURNACE_ITEMS = registerItemTag("furnaces"),
        TORCH_ITEMS = registerItemTag("torches"),
        LANTERN_ITEMS = registerItemTag("lanterns"),
        BRUSHABLE_BLOCK_ITEMS = registerItemTag("brushable_blocks"),
        CAMPFIRE_ITEMS = registerItemTag("campfires"),
        PRESSURE_PLATE_ITEMS = registerItemTag("pressure_plates"),
        STONE_PRESSURE_PLATE_ITEMS = registerItemTag("stone_pressure_plates"),

        // endregion

        // region Interactable Items

        DUPLICATES_ALLAYS = registerItemTag("duplicates_allays"),
        POISONS_PARROTS = registerItemTag("poisons_parrots"),
        REPAIRS_IRON_GOLEMS = registerItemTag("repairs_iron_golems"),
        PIGLIN_BARTERING_ITEMS = registerItemTag("piglin_bartering_items"),
        CHARGES_RESPAWN_ANCHORS = registerItemTag("charges_respawn_anchors"),
        TNT_IGNITERS = registerItemTag("tnt_igniters"),

        // endregion

        // region Equippable Items

        HELMETS = registerItemTag("helmets"),
        CHESTPLATES = registerItemTag("chestplates"),
        LEGGINGS = registerItemTag("leggings"),
        BOOTS = registerItemTag("boots"),
        HORSE_ARMOR = registerItemTag("horse_armor"),
        ELYTRA = registerItemTag("elytra"),
        PIGLIN_PACIFYING_ARMOR = registerItemTag("piglin_pacifying_armor"),
        PROTECTS_AGAINST_ENDERMEN = registerItemTag("protects_against_endermen"),
        POWDER_SNOW_WALKABLE_ITEMS = registerItemTag("powder_snow_walkable_items"),

        // endregion

        // region Item Entities

        IMMUNE_TO_FIRE = registerItemTag("immune_to_fire"),
        IMMUNE_TO_EXPLOSIONS = registerItemTag("immune_to_explosions"),
        IMMUNE_TO_DAMAGE = registerItemTag("immune_to_damage"),
        IMMUNE_TO_DESPAWNING = registerItemTag("immune_to_despawning"),
        INCREASED_DESPAWN_TIME = registerItemTag("increased_despawn_time");

        // endregion
    ;

    /**
     * Block Tags
     **/

    public static final TagKey<Block>

        // region Common Blocks

        AMETHYST_BLOCKS = registerBlockTag("amethyst_blocks"),
        COPPER_BLOCKS = registerBlockTag("copper_blocks"),
        SOUL_SAND = registerBlockTag("soul_sand"),
        PURPUR = registerBlockTag("purpur"),
        PRISMARINE_BLOCKS = registerBlockTag("prismarine_blocks"),
        OBSIDIAN = registerBlockTag("obsidian"),
        HEADS = registerBlockTag("heads"),
        CONCRETE = registerBlockTag("concrete"),
        CONCRETE_POWDER = registerBlockTag("concrete_powder"),
        GLAZED_TERRACOTTA = registerBlockTag("glazed_terracotta"),
        FURNACES = registerBlockTag("furnaces"),
        TORCHES = registerBlockTag("torches"),
        LANTERNS = registerBlockTag("lanterns"),
        CONTAINERS = registerBlockTag("containers"),
        UNBREAKABLE_BLOCKS = registerBlockTag("unbreakable_blocks"),
        WAXABLE_BLOCKS = registerBlockTag("waxable_blocks"),
        WAXED_BLOCKS = registerBlockTag("waxed_blocks"),
        BRUSHABLE_BLOCKS = registerBlockTag("brushable_blocks"),

        // endregion

        // region Technical

        BASE_STONE_END = registerBlockTag("base_stone_end"),
        CHORUS_PLANT_CAN_GROW_ON = registerBlockTag("chorus_plant_can_grow_on"),
        NETHER_WART_CAN_GROW_ON = registerBlockTag("nether_wart_can_grow_on"),
        END_CRYSTAL_BASE_BLOCKS = registerBlockTag("end_crystal_base_blocks"),
        NETHER_PORTAL_BASE_BLOCKS = registerBlockTag("nether_portal_base_blocks"),
        TURTLE_EGGS_CAN_HATCH_ON = registerBlockTag("turtle_eggs_can_hatch_on"),
        ALLOWS_BEACON_BEAM_PASSTHROUGH = registerBlockTag("allows_beacon_beam_passthrough"),
        CANNOT_BE_PUSHED_BY_PISTONS = registerBlockTag("cannot_be_pushed_by_pistons"),
        MOBS_SHOULD_PATHFIND_AROUND = registerBlockTag("mobs_should_pathfind_around"),

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

        ARTHROPODS = registerEntityTag("arthropods"),
        AQUATIC = registerEntityTag("aquatic"),
        VILLAGERS = registerEntityTag("villagers"),
        ILLAGERS = registerEntityTag("illagers"),
        ENDER = registerEntityTag("ender"),
        PIGLINS = registerEntityTag("piglins"),
        SLIMES = registerEntityTag("slimes"),
        GOLEMS = registerEntityTag("golems"),

        // endregion

        // region Non-Living Entities

        IS_VEHICLE = registerEntityTag("is_vehicle"),
        ATTACKABLE_STATIC_ENTITIES = registerEntityTag("attackable_static_entities"),

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
        IS_RANGED = registerDamageTypeTag("is_ranged");
    ;

    /**
     * Biome Tags
     **/

    public static final TagKey<Biome>
        WITHOUT_MOB_SPAWNS = registerBiomeTag("without_mob_spawns");
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