package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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

    public static final TagKey<Item> DUPLICATES_ALLAYS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "duplicates_allays"));
    public static final TagKey<Item> ELYTRA = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "elytra"));
    public static final TagKey<Item> FILLED_BOTTLES = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "filled_bottles"));
    public static final TagKey<Item> FILLED_BUCKETS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "filled_buckets"));
    public static final TagKey<Item> FISH_BUCKETS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "fish_buckets"));
    public static final TagKey<Item> FILLED_BOWLS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "filled_bowls"));
    public static final TagKey<Item> PIGLIN_BARTERING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "piglin_bartering_items"));
    public static final TagKey<Item> PIGLIN_PACIFYING_ARMOR = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "piglin_pacifying_armor"));
    public static final TagKey<Item> SEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "seeds"));
    public static final TagKey<Item> SOUL_SAND_BLOCKS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Item> TELEPORTING_FOODS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "teleporting_foods"));
    public static final TagKey<Item> IMMUNE_TO_DAMAGE = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_damage"));
    public static final TagKey<Item> IMMUNE_TO_DESPAWNING = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_despawning"));
    public static final TagKey<Item> IMMUNE_TO_EXPLOSIONS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_explosions"));
    public static final TagKey<Item> IMMUNE_TO_FIRE = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_fire"));
    public static final TagKey<Item> INCREASED_DESPAWN_TIME = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "increased_despawn_time"));
    public static final TagKey<Item> INSTRUMENTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "instruments"));
    public static final TagKey<Item> GLASS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "glass"));
    public static final TagKey<Item> GLASS_PANES_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "glass_panes"));
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "dyes"));
    public static final TagKey<Item> REPAIRS_IRON_GOLEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "repairs_iron_golems"));
    public static final TagKey<Item> DISC_FRAGMENTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "disc_fragments"));
    public static final TagKey<Item> POISONS_PARROTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "poisons_parrots"));
    public static final TagKey<Item> HEADS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "heads"));
    public static final TagKey<Item> CONCRETE_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "concrete"));
    public static final TagKey<Item> CONCRETE_POWDER_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "concrete_powder"));
    public static final TagKey<Item> GLAZED_TERRACOTTA_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "glazed_terracotta"));
    public static final TagKey<Item> PROTECTS_AGAINST_ENDERMEN = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "protects_against_endermen"));
    public static final TagKey<Item> FURNACES_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "furnaces"));
    public static final TagKey<Item> TORCHES_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "torches"));
    public static final TagKey<Item> LANTERNS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "lanterns"));
    public static final TagKey<Item> MEATS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "meats"));
    public static final TagKey<Item> DUSTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "dusts"));
    public static final TagKey<Item> INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "ingots"));
    public static final TagKey<Item> NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "nuggets"));
    public static final TagKey<Item> COPPER_BLOCKS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "copper_blocks"));
    public static final TagKey<Item> HELMETS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "helmets"));
    public static final TagKey<Item> CHESTPLATES = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "chestplates"));
    public static final TagKey<Item> LEGGINGS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "leggings"));
    public static final TagKey<Item> BOOTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "boots"));
    public static final TagKey<Item> HORSE_ARMOR = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "horse_armor"));
    public static final TagKey<Item> CHARGES_RESPAWN_ANCHORS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "charges_respawn_anchors"));
    public static final TagKey<Item> TNT_IGNITERS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "tnt_igniters"));
    public static final TagKey<Item> POWDER_SNOW_WALKABLE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "powder_snow_walkable_items"));

    /**
     * Block Tags
     **/

    public static final TagKey<Block> AMETHYST_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "amethyst_blocks"));
    public static final TagKey<Block> BASE_STONE_END = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "base_stone_end"));
    public static final TagKey<Block> CHORUS_PLANT_CAN_GROW_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "chorus_plant_can_grow_on"));
    public static final TagKey<Block> NETHER_WART_CAN_GROW_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "nether_wart_can_grow_on"));
    public static final TagKey<Block> CONTAINERS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "containers"));
    public static final TagKey<Block> END_CRYSTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "end_crystal_base_blocks"));
    public static final TagKey<Block> ENTITIES_SHOULD_WALK_AROUND = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "entities_should_walk_around"));
    public static final TagKey<Block> FLYING_MOB_UNSAFE_TO_LAND_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "flying_mob_unsafe_to_land_on"));
    public static final TagKey<Block> MINEABLE_WITH_SWORD = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "mineable/sword"));
    public static final TagKey<Block> MINEABLE_WITH_SHEARS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "mineable/shears"));
    public static final TagKey<Block> NETHER_PORTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "nether_portal_base_blocks"));
    public static final TagKey<Block> SOUL_SAND_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Block> UNSAFE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "unsafe_blocks"));
    public static final TagKey<Block> GLASS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "glass"));
    public static final TagKey<Block> GLASS_PANES = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "glass_panes"));
    public static final TagKey<Block> TURTLE_EGGS_CAN_HATCH_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "turtle_eggs_can_hatch_on"));
    public static final TagKey<Block> HEADS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "heads"));
    public static final TagKey<Block> CONCRETE = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "concrete"));
    public static final TagKey<Block> CONCRETE_POWDER = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "concrete_powder"));
    public static final TagKey<Block> GLAZED_TERRACOTTA = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "glazed_terracotta"));
    public static final TagKey<Block> FURNACES = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "furnaces"));
    public static final TagKey<Block> TORCHES = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "torches"));
    public static final TagKey<Block> LANTERNS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "lanterns"));
    public static final TagKey<Block> COPPER_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "copper_blocks"));
    public static final TagKey<Block> ALLOWS_BEACON_BEAM_PASSTHROUGH = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "allows_beacon_beam_passthrough"));
    public static final TagKey<Block> UNBREAKABLE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "unbreakable_blocks"));
    public static final TagKey<Block> CANNOT_BE_PUSHED_BY_PISTONS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "cannot_be_pushed_by_pistons"));
    public static final TagKey<Block> WAXABLE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "waxable_blocks"));
    public static final TagKey<Block> WAXED_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "waxed_blocks"));

    /**
     * Entity Tags
     **/

    public static final TagKey<EntityType<?>> IS_VEHICLE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "is_vehicle"));
    public static final TagKey<EntityType<?>> ATTACKABLE_NON_LIVING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "attackable_non_living_entities"));
    public static final TagKey<EntityType<?>> CANNOT_RIDE_BOATS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "cannot_ride_boats"));
    public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "undead"));
    public static final TagKey<EntityType<?>> ARTHROPODS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "arthropods"));
    public static final TagKey<EntityType<?>> AQUATIC = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "aquatic"));
    public static final TagKey<EntityType<?>> VILLAGERS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "villagers"));
    public static final TagKey<EntityType<?>> ILLAGERS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "illagers"));
    public static final TagKey<EntityType<?>> ENDER = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "ender"));
    public static final TagKey<EntityType<?>> PIGLINS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "piglins"));
    public static final TagKey<EntityType<?>> SLIMES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "slimes"));
    public static final TagKey<EntityType<?>> GOLEMS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "golems"));

    /**
     * Damage Type Tags
     **/

    public static final TagKey<DamageType> IS_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "is_magic"));

    /**
     * Biome Tags
     **/

    public static final TagKey<Biome> WITHOUT_MOB_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(Chrysalis.MOD_ID, "without_mob_spawns"));
}
