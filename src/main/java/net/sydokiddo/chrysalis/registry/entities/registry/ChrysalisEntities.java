package net.sydokiddo.chrysalis.registry.entities.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.entities.custom_entities.Seat;
import java.util.function.Supplier;

public class ChrysalisEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(ChrysalisMod.MOD_ID);

    // region Entities

    public static final Supplier<EntityType<Seat>> SEAT = registerEntityType("seat",
        EntityType.Builder.of(Seat::new, MobCategory.MISC).sized(0.0F, 0.0F).noLootTable().clientTrackingRange(10));

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, ChrysalisMod.id(name));
        return ENTITY_TYPES.register(name, () -> builder.build(resourceKey));
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    // endregion
}