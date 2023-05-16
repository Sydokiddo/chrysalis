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

    // Item Tags:

    public static final TagKey<Item> DUPLICATES_ALLAYS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "duplicates_allays"));
    public static final TagKey<Item> ELYTRA = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "elytra"));
    public static final TagKey<Item> FILLED_BOTTLES = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "filled_bottles"));
    public static final TagKey<Item> FILLED_BUCKETS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "filled_buckets"));
    public static final TagKey<Item> PIGLIN_BARTERING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "piglin_bartering_items"));
    public static final TagKey<Item> PIGLIN_PACIFYING_ARMOR = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "piglin_pacifying_armor"));
    public static final TagKey<Item> SEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "seeds"));
    public static final TagKey<Item> SOUL_SAND_BLOCKS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Item> TELEPORTING_FOODS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "teleporting_foods"));
    public static final TagKey<Item> IMMUNE_TO_DAMAGE = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_damage"));
    public static final TagKey<Item> IMMUNE_TO_DESPAWNING = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_despawning"));
    public static final TagKey<Item> IMMUNE_TO_EXPLOSIONS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_explosions"));
    public static final TagKey<Item> INCREASED_DESPAWN_TIME = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "increased_despawn_time"));
    public static final TagKey<Item> INSTRUMENTS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "instruments"));

    // Block Tags:

    public static final TagKey<Block> AMETHYST_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "amethyst_blocks"));
    public static final TagKey<Block> BASE_STONE_END = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "base_stone_end"));
    public static final TagKey<Block> CHORUS_PLANT_CAN_GROW_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "chorus_plant_can_grow_on"));
    public static final TagKey<Block> CONTAINERS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "containers"));
    public static final TagKey<Block> END_CRYSTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "end_crystal_base_blocks"));
    public static final TagKey<Block> ENTITIES_SHOULD_WALK_AROUND = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "entities_should_walk_around"));
    public static final TagKey<Block> FLYING_MOB_UNSAFE_TO_LAND_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "flying_mob_unsafe_to_land_on"));
    public static final TagKey<Block> MINED_FASTER_WITH_SWORDS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "mined_faster_with_swords"));
    public static final TagKey<Block> NETHER_PORTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "nether_portal_base_blocks"));
    public static final TagKey<Block> SOUL_SAND_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Block> UNSAFE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "unsafe_blocks"));

    // Entity Tags:

    public static final TagKey<EntityType<?>> ATTACKABLE_NON_LIVING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "attackable_non_living_entities"));
    public static final TagKey<EntityType<?>> CANNOT_RIDE_BOATS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "cannot_ride_boats"));
    public static final TagKey<EntityType<?>> ENDER_RELATED_MOBS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "ender_related_mobs"));
    public static final TagKey<EntityType<?>> IS_VEHICLE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "is_vehicle"));

    // Damage Type Tags:

    public static final TagKey<DamageType> IS_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "is_magic"));

    // Biome Tags:

    public static final TagKey<Biome> WITHOUT_MOB_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(Chrysalis.MOD_ID, "without_mob_spawns"));
}
