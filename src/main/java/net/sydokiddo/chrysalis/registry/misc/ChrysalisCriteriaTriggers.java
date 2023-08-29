package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisCriteriaTriggers extends CriteriaTriggers {

    // List of Criteria Triggers:

    public static final PlayerTrigger DUPLICATE_ALLAY = ChrysalisCriteriaTriggers.register(new PlayerTrigger(Chrysalis.id("duplicate_allay")));

    // Registry for Criteria Triggers:

    public static void registerCriteriaTriggers() {}
}