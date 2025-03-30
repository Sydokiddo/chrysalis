package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.CItems;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import java.util.function.Supplier;

public class CCreativeModeTabs {

    /**
     * Registers the debug utility items in the creative mode inventory.
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
            output.accept(CItems.TELEPORT_WAND);
            output.accept(CItems.KILL_WAND);
            output.accept(CItems.AGGRO_WAND);
            output.accept(CItems.TAME_MOB);
            output.accept(CItems.RIDE_MOB);
            output.accept(CItems.COPYING_SPAWN_EGG);
            if (Chrysalis.IS_DEBUG && Chrysalis.registerTestItems) output.accept(CItems.TEST_RIGHT_CLICK_ITEM);
        })
    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}