package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("ALL")
public class ChrysalisTags {

    // Item Tags:

    public static final TagKey<Item> ELYTRA = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "elytra"));
    public static final TagKey<Item> PIGLIN_BARTERING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "piglin_bartering_items"));
    public static final TagKey<Item> SEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "seeds"));
    public static final TagKey<Item> SOUL_SAND_BLOCKS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Item> TELEPORTING_FOODS = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "teleporting_foods"));
    public static final TagKey<Item> IMMUNE_TO_DAMAGE = TagKey.create(Registries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, "immune_to_damage"));

    // Block Tags:

    public static final TagKey<Block> BASE_STONE_END = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "base_stone_end"));
    public static final TagKey<Block> CHORUS_PLANT_CAN_GROW_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "chorus_plant_can_grow_on"));
    public static final TagKey<Block> CONTAINERS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "containers"));
    public static final TagKey<Block> END_CRYSTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "end_crystal_base_blocks"));
    public static final TagKey<Block> NETHER_PORTAL_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "nether_portal_base_blocks"));
    public static final TagKey<Block> SOUL_SAND_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "soul_sand_blocks"));
    public static final TagKey<Block> UNSAFE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "unsafe_blocks"));
    public static final TagKey<Block> FLYING_MOB_UNSAFE_TO_LAND_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "flying_mob_unsafe_to_land_on"));
    public static final TagKey<Block> ENTITIES_SHOULD_WALK_AROUND = TagKey.create(Registries.BLOCK, new ResourceLocation(Chrysalis.MOD_ID, "entities_should_walk_around"));

    // Entity Tags:

    public static final TagKey<EntityType<?>> ATTACKABLE_NON_LIVING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "attackable_non_living_entities"));
    public static final TagKey<EntityType<?>> CANNOT_RIDE_BOATS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "cannot_ride_boats"));
    public static final TagKey<EntityType<?>> ENDER_RELATED_MOBS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Chrysalis.MOD_ID, "ender_related_mobs"));

    // Biome Tags:

    public static final TagKey<Biome> WITHOUT_MOB_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(Chrysalis.MOD_ID, "without_mob_spawns"));
}
