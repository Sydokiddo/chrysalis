package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisGameEvents {

    // List of Game Events:

    public static final GameEvent ENTITY_FLUTTER = registerGameEvent("entity_flutter");
    public static final GameEvent ENTITY_PUFF = registerGameEvent("entity_puff");
    public static final GameEvent ENTITY_SMASH = registerGameEvent("entity_smash");

    // Registry for Game Events:

    private static GameEvent registerGameEvent(String name) {
        return Registry.register(BuiltInRegistries.GAME_EVENT, Chrysalis.id(name), new GameEvent(name, 16));
    }

    public static void registerGameEvents() {}
}