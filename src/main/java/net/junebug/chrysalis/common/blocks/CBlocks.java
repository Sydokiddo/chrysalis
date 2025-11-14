package net.junebug.chrysalis.common.blocks;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
import net.junebug.chrysalis.common.items.CItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Function;

public class CBlocks {

    /**
     * The registry for blocks added by chrysalis.
     **/

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chrysalis.MOD_ID);

    public static final DeferredBlock<Block>
        PLACEHOLDER_BLOCK = registerBlock("placeholder_block", PlaceholderBlock::new, BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).mapColor(MapColor.COLOR_LIGHT_GRAY).noLootTable().noOcclusion().isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK), new Item.Properties().rarity(Rarity.EPIC))
    ;

    @SuppressWarnings("all")
    private static DeferredBlock<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, function, blockSettings);
        CItems.ITEMS.registerSimpleBlockItem(block, itemSettings);
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}