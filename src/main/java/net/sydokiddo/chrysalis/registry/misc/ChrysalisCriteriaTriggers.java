package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ChrysalisCriteriaTriggers extends CriteriaTriggers {

    // Criteria Triggers

    public static final PlayerTrigger DUPLICATE_MOB = ChrysalisCriteriaTriggers.register("chrysalis:duplicate_mob", new PlayerTrigger());
    public static final PlayerTrigger CURE_POISON_WITH_HONEY = ChrysalisCriteriaTriggers.register("chrysalis:cure_poison_with_honey", new PlayerTrigger());
    public static final PlayerTrigger USE_RIPTIDE_TRIDENT = ChrysalisCriteriaTriggers.register("chrysalis:use_riptide_trident", new PlayerTrigger());

    // Registry

    public static void registerCriteriaTriggers() {}
}