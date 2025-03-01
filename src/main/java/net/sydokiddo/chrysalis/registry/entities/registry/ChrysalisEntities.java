package net.sydokiddo.chrysalis.registry.entities.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.entities.custom_entities.Seat;

public class ChrysalisEntities {

    // region Entities

    public static final EntityType<Seat> SEAT = registerEntityType("seat", EntityType.Builder.of(Seat::new, MobCategory.MISC).sized(0.0F, 0.0F).noLootTable().clientTrackingRange(10));

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, ChrysalisMod.id(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, resourceKey, builder.build(resourceKey));
    }

    public static void registerEntities() {}

    // endregion
}