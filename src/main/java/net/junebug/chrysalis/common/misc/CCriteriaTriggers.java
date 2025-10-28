package net.junebug.chrysalis.common.misc;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;

public class CCriteriaTriggers {

    private static final DeferredRegister<CriterionTrigger<?>> CRITERIA_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Chrysalis.MOD_ID);

    // region Criteria Triggers

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger>
        USE_RIPTIDE_TRIDENT = CRITERIA_TRIGGERS.register("use_riptide_trident", PlayerTrigger::new)
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        CRITERIA_TRIGGERS.register(eventBus);
    }

    // endregion
}