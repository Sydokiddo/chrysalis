package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ChrysalisCriteriaTriggers extends CriteriaTriggers {

    // List of Criteria Triggers:

    public static final PlayerTrigger DUPLICATE_ALLAY = ChrysalisCriteriaTriggers.register("chrysalis:duplicate_allay", new PlayerTrigger());

    // Registry for Criteria Triggers:

    public static void registerCriteriaTriggers() {}
}