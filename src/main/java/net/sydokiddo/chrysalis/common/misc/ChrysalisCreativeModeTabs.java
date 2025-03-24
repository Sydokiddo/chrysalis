package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.ChrysalisItems;
import java.util.function.Supplier;

public class ChrysalisCreativeModeTabs {

    /**
     * Registers the debug utility items in the creative mode inventory.
     **/

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chrysalis.MOD_ID);

    @SuppressWarnings("unused")
    public static final Supplier<CreativeModeTab> CHRYSALIS_TAB = CREATIVE_MODE_TAB.register("chrysalis_tab", () -> CreativeModeTab.builder()
        .icon(() -> new ItemStack(ChrysalisItems.ICON.get()))
        .title(Component.translatable("mod.chrysalis"))
        .displayItems((parameters, output) -> {
            output.accept(ChrysalisItems.HEAL);
            output.accept(ChrysalisItems.FILL_HUNGER);
            output.accept(ChrysalisItems.FILL_OXYGEN);
            output.accept(ChrysalisItems.GIVE_RESISTANCE);
            output.accept(ChrysalisItems.CLEAR_EFFECTS);
            output.accept(ChrysalisItems.TELEPORT_TO_SPAWNPOINT);
            output.accept(ChrysalisItems.TELEPORT_WAND);
            output.accept(ChrysalisItems.KILL_WAND);
            output.accept(ChrysalisItems.AGGRO_WAND);
            output.accept(ChrysalisItems.TAME_MOB);
            output.accept(ChrysalisItems.RIDE_MOB);
            if (Chrysalis.IS_DEBUG && Chrysalis.registerTestItems) output.accept(ChrysalisItems.TEST_RIGHT_CLICK_ITEM);
        })
    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}