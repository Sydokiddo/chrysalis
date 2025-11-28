package net.junebug.chrysalis.common.entities.registry;

import net.junebug.chrysalis.common.entities.custom_entities.EmptyEntity;
import net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake.Earthquake;
import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.encounter_spawner.EncounterSpawner;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawner;
import net.junebug.chrysalis.common.entities.custom_entities.Seat;

public class CEntities {

    /**
     * The registry for entities added by chrysalis.
     **/

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(Chrysalis.MOD_ID);

    // region Entities

    public static final DeferredHolder<EntityType<?>, EntityType<EmptyEntity>> EMPTY = registerEntityType("empty",
        EntityType.Builder.of(EmptyEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).noSummon().noSave().noLootTable());

    public static final DeferredHolder<EntityType<?>, EntityType<Seat>> SEAT = registerEntityType("seat",
        EntityType.Builder.of(Seat::new, MobCategory.MISC).sized(0.0F, 0.0F).noLootTable().clientTrackingRange(10));

    public static final DeferredHolder<EntityType<?>, EntityType<EntitySpawner>> ENTITY_SPAWNER = registerEntityType("entity_spawner",
        EntityType.Builder.of(EntitySpawner::new, MobCategory.MISC).sized(0.25F, 0.25F).noLootTable().clientTrackingRange(8));

    public static final DeferredHolder<EntityType<?>, EntityType<EncounterSpawner>> ENCOUNTER_SPAWNER = registerEntityType("encounter_spawner",
        EntityType.Builder.of(EncounterSpawner::new, MobCategory.MISC).sized(0.75F, 0.75F).noLootTable().clientTrackingRange(8));

    public static final DeferredHolder<EntityType<?>, EntityType<Earthquake>> EARTHQUAKE = registerEntityType("earthquake",
        EntityType.Builder.of(Earthquake::new, MobCategory.MISC).sized(1.5F, 0.5F).noLootTable().clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));

    public static final DeferredHolder<EntityType<?>, EntityType<KeyGolem>> KEY_GOLEM = registerEntityType("key_golem",
        EntityType.Builder.of(KeyGolem::new, MobCategory.CREATURE).sized(0.6F, 0.9F).eyeHeight(0.55F).fireImmune().clientTrackingRange(10));

    // endregion

    // region Registry

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(KEY_GOLEM.get(), KeyGolem.createAttributes().build());
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, Chrysalis.resourceLocationId(name));
        return ENTITY_TYPES.register(name, () -> builder.build(resourceKey));
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    // endregion
}