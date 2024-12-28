package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ChrysalisCriteriaTriggers extends CriteriaTriggers {

    // region Criteria Triggers

    public static final PlayerTrigger USE_RIPTIDE_TRIDENT = ChrysalisCriteriaTriggers.register("chrysalis:use_riptide_trident", new PlayerTrigger());

    // endregion

    // region Registry

    public static void registerCriteriaTriggers() {}

    // endregion
}