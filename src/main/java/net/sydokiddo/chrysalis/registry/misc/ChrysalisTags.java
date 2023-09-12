package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("ALL")
public class ChrysalisTags {

    /**
     * Item Tags
     **/

    // Common Items

    public static final TagKey<Item> DISC_FRAGMENTS = registerItemTag("disc_fragments");
    public static final TagKey<Item> INSTRUMENTS = registerItemTag("instruments");
    public static final TagKey<Item> SEEDS = registerItemTag("seeds");
    public static final TagKey<Item> TELEPORTING_FOODS = registerItemTag("teleporting_foods");
    public static final TagKey<Item> MEATS = registerItemTag("meats");
    public static final TagKey<Item> FILLED_BOTTLES = registerItemTag("filled_bottles");
    public static final TagKey<Item> FILLED_BUCKETS = registerItemTag("filled_buckets");
    public static final TagKey<Item> FISH_BUCKETS = registerItemTag("fish_buckets");
    public static final TagKey<Item> FILLED_BOWLS = registerItemTag("filled_bowls");
    public static final TagKey<Item> BRICKS = registerItemTag("bricks");
    public static final TagKey<Item> BANNER_PATTERNS = registerItemTag("banner_patterns");

    // Block Items

    public static final TagKey<Item> AMETHYST_BLOCK_ITEMS = registerItemTag("amethyst_blocks");
    public static final TagKey<Item> SOUL_SAND_ITEMS = registerItemTag("soul_sand");
    public static final TagKey<Item> CONCRETE_ITEMS = registerItemTag("concrete");
    public static final TagKey<Item> CONCRETE_POWDER_ITEMS = registerItemTag("concrete_powder");
    public static final TagKey<Item> GLAZED_TERRACOTTA_ITEMS = registerItemTag("glazed_terracotta");
    public static final TagKey<Item> COPPER_BLOCK_ITEMS = registerItemTag("copper_blocks");
    public static final TagKey<Item> PRISMARINE_BLOCK_ITEMS = registerItemTag("prismarine_blocks");
    public static final TagKey<Item> OBSIDIAN_ITEMS = registerItemTag("obsidian");
    public static final TagKey<Item> PURPUR_ITEMS = registerItemTag("purpur");
    public static final TagKey<Item> HEAD_ITEMS = registerItemTag("heads");
    public static final TagKey<Item> CONTAINER_ITEMS = registerItemTag("containers");
    public static final TagKey<Item> FURNACE_ITEMS = registerItemTag("furnaces");
    public static final TagKey<Item> TORCH_ITEMS = registerItemTag("torches");
    public static final TagKey<Item> LANTERN_ITEMS = registerItemTag("lanterns");
    public static final TagKey<Item> BRUSHABLE_BLOCK_ITEMS = registerItemTag("brushable_blocks");
    public static final TagKey<Item> CAMPFIRE_ITEMS = registerItemTag("campfires");
    public static final TagKey<Item> PRESSURE_PLATE_ITEMS = registerItemTag("pressure_plates");
    public static final TagKey<Item> STONE_PRESSURE_PLATE_ITEMS = registerItemTag("stone_pressure_plates");

    // Interactable Items

    public static final TagKey<Item> DUPLICATES_ALLAYS = registerItemTag("duplicates_allays");
    public static final TagKey<Item> POISONS_PARROTS = registerItemTag("poisons_parrots");
    public static final TagKey<Item> REPAIRS_IRON_GOLEMS = registerItemTag("repairs_iron_golems");
    public static final TagKey<Item> PIGLIN_BARTERING_ITEMS = registerItemTag("piglin_bartering_items");
    public static final TagKey<Item> CHARGES_RESPAWN_ANCHORS = registerItemTag("charges_respawn_anchors");
    public static final TagKey<Item> TNT_IGNITERS = registerItemTag("tnt_igniters");

    // Equippable Items

    public static final TagKey<Item> HELMETS = registerItemTag("helmets");
    public static final TagKey<Item> CHESTPLATES = registerItemTag("chestplates");
    public static final TagKey<Item> LEGGINGS = registerItemTag("leggings");
    public static final TagKey<Item> BOOTS = registerItemTag("boots");
    public static final TagKey<Item> HORSE_ARMOR = registerItemTag("horse_armor");
    public static final TagKey<Item> ELYTRA = registerItemTag("elytra");
    public static final TagKey<Item> PIGLIN_PACIFYING_ARMOR = registerItemTag("piglin_pacifying_armor");
    public static final TagKey<Item> PROTECTS_AGAINST_ENDERMEN = registerItemTag("protects_against_endermen");
    public static final TagKey<Item> POWDER_SNOW_WALKABLE_ITEMS = registerItemTag("powder_snow_walkable_items");

    // Item Entities

    public static final TagKey<Item> IMMUNE_TO_FIRE = registerItemTag("immune_to_fire");
    public static final TagKey<Item> IMMUNE_TO_EXPLOSIONS = registerItemTag("immune_to_explosions");
    public static final TagKey<Item> IMMUNE_TO_DAMAGE = registerItemTag("immune_to_damage");
    public static final TagKey<Item> IMMUNE_TO_DESPAWNING = registerItemTag("immune_to_despawning");
    public static final TagKey<Item> INCREASED_DESPAWN_TIME = registerItemTag("increased_despawn_time");

    /**
     * Block Tags
     **/

    // Common Blocks

    public static final TagKey<Block> AMETHYST_BLOCKS = registerBlockTag("amethyst_blocks");
    public static final TagKey<Block> COPPER_BLOCKS = registerBlockTag("copper_blocks");
    public static final TagKey<Block> SOUL_SAND = registerBlockTag("soul_sand");
    public static final TagKey<Block> PURPUR = registerBlockTag("purpur");
    public static final TagKey<Block> PRISMARINE_BLOCKS = registerBlockTag("prismarine_blocks");
    public static final TagKey<Block> OBSIDIAN = registerBlockTag("obsidian");
    public static final TagKey<Block> HEADS = registerBlockTag("heads");
    public static final TagKey<Block> CONCRETE = registerBlockTag("concrete");
    public static final TagKey<Block> CONCRETE_POWDER = registerBlockTag("concrete_powder");
    public static final TagKey<Block> GLAZED_TERRACOTTA = registerBlockTag("glazed_terracotta");
    public static final TagKey<Block> FURNACES = registerBlockTag("furnaces");
    public static final TagKey<Block> TORCHES = registerBlockTag("torches");
    public static final TagKey<Block> LANTERNS = registerBlockTag("lanterns");
    public static final TagKey<Block> CONTAINERS = registerBlockTag("containers");
    public static final TagKey<Block> UNBREAKABLE_BLOCKS = registerBlockTag("unbreakable_blocks");
    public static final TagKey<Block> WAXABLE_BLOCKS = registerBlockTag("waxable_blocks");
    public static final TagKey<Block> WAXED_BLOCKS = registerBlockTag("waxed_blocks");
    public static final TagKey<Block> BRUSHABLE_BLOCKS = registerBlockTag("brushable_blocks");

    // Technical

    public static final TagKey<Block> BASE_STONE_END = registerBlockTag("base_stone_end");
    public static final TagKey<Block> CHORUS_PLANT_CAN_GROW_ON = registerBlockTag("chorus_plant_can_grow_on");
    public static final TagKey<Block> NETHER_WART_CAN_GROW_ON = registerBlockTag("nether_wart_can_grow_on");
    public static final TagKey<Block> END_CRYSTAL_BASE_BLOCKS = registerBlockTag("end_crystal_base_blocks");
    public static final TagKey<Block> NETHER_PORTAL_BASE_BLOCKS = registerBlockTag("nether_portal_base_blocks");
    public static final TagKey<Block> TURTLE_EGGS_CAN_HATCH_ON = registerBlockTag("turtle_eggs_can_hatch_on");
    public static final TagKey<Block> ALLOWS_BEACON_BEAM_PASSTHROUGH = registerBlockTag("allows_beacon_beam_passthrough");
    public static final TagKey<Block> CANNOT_BE_PUSHED_BY_PISTONS = registerBlockTag("cannot_be_pushed_by_pistons");
    public static final TagKey<Block> MOBS_SHOULD_PATHFIND_AROUND = registerBlockTag("mobs_should_pathfind_around");

    // Mineable Block Tags

    public static final TagKey<Block> MINEABLE_WITH_SWORD = registerBlockTag("mineable/sword");
    public static final TagKey<Block> MINEABLE_WITH_SHEARS = registerBlockTag("mineable/shears");

    /**
     * Entity Tags
     **/

    // Living Entities

    public static final TagKey<EntityType<?>> UNDEAD = registerEntityTag("undead");
    public static final TagKey<EntityType<?>> ARTHROPODS = registerEntityTag("arthropods");
    public static final TagKey<EntityType<?>> AQUATIC = registerEntityTag("aquatic");
    public static final TagKey<EntityType<?>> VILLAGERS = registerEntityTag("villagers");
    public static final TagKey<EntityType<?>> ILLAGERS = registerEntityTag("illagers");
    public static final TagKey<EntityType<?>> ENDER = registerEntityTag("ender");
    public static final TagKey<EntityType<?>> PIGLINS = registerEntityTag("piglins");
    public static final TagKey<EntityType<?>> SLIMES = registerEntityTag("slimes");
    public static final TagKey<EntityType<?>> GOLEMS = registerEntityTag("golems");

    // Non-Living Entitites

    public static final TagKey<EntityType<?>> IS_VEHICLE = registerEntityTag("is_vehicle");
    public static final TagKey<EntityType<?>> ATTACKABLE_STATIC_ENTITIES = registerEntityTag("attackable_static_entities");

    // Technical

    public static final TagKey<EntityType<?>> CANNOT_RIDE_BOATS = registerEntityTag("cannot_ride_boats");

    /**
     * Damage Type Tags
     **/

    public static final TagKey<DamageType> IS_MAGIC = registerDamageTypeTag("is_magic");
    public static final TagKey<DamageType> IS_RANGED = registerDamageTypeTag("is_ranged");

    /**
     * Biome Tags
     **/

    public static final TagKey<Biome> WITHOUT_MOB_SPAWNS = registerBiomeTag("without_mob_spawns");

    /**
     * Registries
     **/

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
}