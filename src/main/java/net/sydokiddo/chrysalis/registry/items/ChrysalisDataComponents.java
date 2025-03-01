package net.sydokiddo.chrysalis.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.CustomData;
import net.sydokiddo.chrysalis.ChrysalisMod;
import java.util.function.UnaryOperator;

public class ChrysalisDataComponents {

    // region Data Components

    public static final DataComponentType<CustomData> LINKED_MOB_DATA = registerDataComponent("linked_mob_data", (builder) -> builder.persistent(CustomData.CODEC));

    // endregion

    // region registry

    @SuppressWarnings("all")
    private static <T> DataComponentType<T> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ChrysalisMod.id(name), unaryOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataComponents() {}

    // endregion
}