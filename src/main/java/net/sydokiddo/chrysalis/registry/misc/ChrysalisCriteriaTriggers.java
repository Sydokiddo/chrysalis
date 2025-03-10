package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.ChrysalisMod;
import java.util.function.Supplier;

public class ChrysalisCriteriaTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, ChrysalisMod.MOD_ID);

    // region Criteria Triggers

    public static final Supplier<PlayerTrigger>
        USE_RIPTIDE_TRIDENT = CRITERIA_TRIGGERS.register("use_riptide_trident", PlayerTrigger::new)
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        CRITERIA_TRIGGERS.register(eventBus);
    }

    // endregion
}