package net.sydokiddo.chrysalis.common.items;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Chrysalis.MOD_ID);

    // region Data Components

    @SuppressWarnings("deprecation")
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> LINKED_MOB_DATA = DATA_COMPONENTS.registerComponentType("linked_mob_data", (builder) -> builder.persistent(CustomData.CODEC_WITH_ID).networkSynchronized(CustomData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>>
        IMMUNE_TO_ALL_DAMAGE = DATA_COMPONENTS.registerComponentType("immune_to_all_damage", (builder) -> builder.persistent(Unit.CODEC)),
        IMMUNE_TO_DESPAWNING = DATA_COMPONENTS.registerComponentType("immune_to_despawning", (builder) -> builder.persistent(Unit.CODEC)),
        INCREASED_DESPAWN_TIME = DATA_COMPONENTS.registerComponentType("increased_despawn_time", (builder) -> builder.persistent(Unit.CODEC)),
        INCREASED_PICK_RADIUS = DATA_COMPONENTS.registerComponentType("increased_pick_radius", (builder) -> builder.persistent(Unit.CODEC)),
        STAYS_ON_DEATH = DATA_COMPONENTS.registerComponentType("stays_on_death", (builder) -> builder.persistent(Unit.CODEC))
    ;

    // endregion

    // region registry

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }

    // endregion
}