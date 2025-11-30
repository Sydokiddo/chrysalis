package net.junebug.chrysalis.common.misc;

import net.junebug.chrysalis.common.blocks.CBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.items.CItems;
import net.junebug.chrysalis.util.technical.config.CConfigOptions;
import java.util.function.Supplier;

public class CCreativeModeTabs {

    /**
     * Registers all items added by chrysalis in the creative mode inventory.
     **/

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chrysalis.MOD_ID);

    @SuppressWarnings("unused")
    private static final Supplier<CreativeModeTab> CHRYSALIS_TAB = CREATIVE_MODE_TAB.register("chrysalis_tab", () -> CreativeModeTab.builder()
        .icon(() -> new ItemStack(CItems.ICON.get()))
        .title(Component.translatable("mod.chrysalis"))
        .displayItems((parameters, output) -> {
            if (!CConfigOptions.CHRYSALIS_CREATIVE_MODE_TAB.get()) return;
            output.accept(CItems.HEAL);
            output.accept(CItems.FILL_HUNGER);
            output.accept(CItems.FILL_OXYGEN);
            output.accept(CItems.GIVE_RESISTANCE);
            output.accept(CItems.CLEAR_EFFECTS);
            output.accept(CItems.TELEPORT_TO_SPAWNPOINT);
            output.accept(CItems.TELEPORT_TO_OVERWORLD);
            output.accept(CItems.TELEPORT_TO_NETHER);
            output.accept(CItems.TELEPORT_TO_END);
            output.accept(CItems.TELEPORT_WAND);
            output.accept(CItems.KILL_WAND);
            output.accept(CItems.AGGRO_WAND);
            output.accept(CItems.TAME_MOB);
            output.accept(CItems.RIDE_MOB);
            output.accept(CItems.PICK_UP_MOB);
            output.accept(CItems.DRAIN_FLUIDS);
            output.accept(CItems.COPYING_SPAWN_EGG);
            output.accept(CItems.KEY);
            output.accept(CItems.ADMIN_KEY);
            output.accept(CItems.MUSIC_EVENT_TEST);
            output.accept(CItems.CAMERA_SHAKE_EVENT_TEST);
            output.accept(CItems.EARTHQUAKE_EVENT_TEST);
            output.accept(CItems.ENTITY_SPAWNER_EVENT_TEST);
            output.accept(CItems.ENCOUNTER_SPAWNER_EVENT_TEST);
            if (Chrysalis.IS_DEBUG && Chrysalis.registerTestItems) output.accept(CItems.TEST_RIGHT_CLICK_ITEM);
            output.accept(CItems.GIANT_SPAWN_EGG);
            output.accept(CItems.ILLUSIONER_SPAWN_EGG);
            output.accept(CItems.KEY_GOLEM_SPAWN_EGG);
            output.accept(CItems.NETHER_PORTAL);
            output.accept(CItems.END_PORTAL);
            output.accept(CItems.END_GATEWAY);
            output.accept(CBlocks.BARRICADE.toStack());
            output.accept(CBlocks.UNBREAKABLE_BARRICADE.toStack());
            output.accept(CBlocks.BARRICADE_BLOCK.toStack());
            output.accept(CBlocks.UNBREAKABLE_BARRICADE_BLOCK.toStack());
            output.accept(CBlocks.PLACEHOLDER_BLOCK.toStack());
        })
    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}