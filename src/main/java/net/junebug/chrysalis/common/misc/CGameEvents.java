package net.junebug.chrysalis.common.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;

public class CGameEvents {

    /**
     * The registry for game events added by chrysalis.
     **/

    private static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, Chrysalis.MOD_ID);

    // region Game Events

    public static final DeferredHolder<GameEvent, GameEvent>
        EMPTY = GAME_EVENTS.register("empty", () -> new GameEvent(0))
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        GAME_EVENTS.register(eventBus);
    }

    // endregion
}