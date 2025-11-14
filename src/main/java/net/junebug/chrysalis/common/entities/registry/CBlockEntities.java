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

    /**
     * The registry for block entities added by chrysalis.
     **/

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Chrysalis.MOD_ID);

    // region Block Entities

    public static final Supplier<BlockEntityType<PlaceholderBlockEntity>> PLACEHOLDER_BLOCK = BLOCK_ENTITY_TYPES.register(
        "placeholder_block", () -> new BlockEntityType<>(PlaceholderBlockEntity::new, CBlocks.PLACEHOLDER_BLOCK.get())
    );

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    // endregion
}