package net.junebug.chrysalis.common.entities.registry;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.blocks.CBlocks;
import net.junebug.chrysalis.common.entities.custom_entities.PlaceholderBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class CBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Chrysalis.MOD_ID);

    public static final Supplier<BlockEntityType<PlaceholderBlockEntity>> PLACEHOLDER = BLOCK_ENTITY_TYPES.register(
        "placeholder", () -> new BlockEntityType<>(PlaceholderBlockEntity::new, CBlocks.PLACEHOLDER.get())
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}