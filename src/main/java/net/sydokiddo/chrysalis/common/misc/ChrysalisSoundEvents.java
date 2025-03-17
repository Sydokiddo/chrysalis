package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import java.util.HashMap;
import java.util.Map;

public class ChrysalisSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Chrysalis.MOD_ID);

    // region Sound Events

    public static final DeferredHolder<SoundEvent, SoundEvent>

        SCREENSHOT_SUCCESS = SOUND_EVENTS.register("ui.screenshot.success", SoundEvent::createVariableRangeEvent),
        SPLASH_TEXT_SHUFFLE = SOUND_EVENTS.register("ui.splash_text.shuffle", SoundEvent::createVariableRangeEvent),

        HEAL_USE = SOUND_EVENTS.register("item.heal.use", SoundEvent::createVariableRangeEvent),
        FILL_HUNGER_USE = SOUND_EVENTS.register("item.fill_hunger.use", SoundEvent::createVariableRangeEvent),
        FILL_OXYGEN_USE = SOUND_EVENTS.register("item.fill_oxygen.use", SoundEvent::createVariableRangeEvent),
        GIVE_RESISTANCE_USE = SOUND_EVENTS.register("item.give_resistance.use", SoundEvent::createVariableRangeEvent),
        CLEAR_EFFECTS_USE = SOUND_EVENTS.register("item.clear_effects.use", SoundEvent::createVariableRangeEvent),
        TELEPORT_TO_SPAWNPOINT_FAIL = SOUND_EVENTS.register("item.teleport_to_spawnpoint.fail", SoundEvent::createVariableRangeEvent),
        TAME_MOB_USE = SOUND_EVENTS.register("item.tame_mob.use", SoundEvent::createVariableRangeEvent),
        RIDE_MOB_USE = SOUND_EVENTS.register("item.ride_mob.use", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_LINK = SOUND_EVENTS.register("item.aggro_wand.link", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_SELECT_TARGET_FAIL = SOUND_EVENTS.register("item.aggro_wand.select_target.fail", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_SELECT_TARGET_SUCCESS = SOUND_EVENTS.register("item.aggro_wand.select_target.success", SoundEvent::createVariableRangeEvent),

        ITEM_DROP = SOUND_EVENTS.register("entity.item.drop", SoundEvent::createVariableRangeEvent),

        ENTITY_SPAWNER_APPEAR = SOUND_EVENTS.register("entity.entity_spawner.appear", SoundEvent::createVariableRangeEvent),
        ENTITY_SPAWNER_ABOUT_TO_SPAWN_ENTITY = SOUND_EVENTS.register("entity.entity_spawner.about_to_spawn_entity", SoundEvent::createVariableRangeEvent),
        ENTITY_SPAWNER_SPAWN_ENTITY = SOUND_EVENTS.register("entity.entity_spawner.spawn_entity", SoundEvent::createVariableRangeEvent)
    ;

    // endregion

    // region Registry

    public static Map<String, Music> structures = new HashMap<>();

    public static void register(IEventBus eventBus) {

        SOUND_EVENTS.register(eventBus);

        for (Map.Entry<String, ChrysalisRegistry.StructureMusicSound> structure : ChrysalisRegistry.registeredStructures.entrySet()) {
            ChrysalisRegistry.StructureMusicSound musicSound = structure.getValue();
            structures.put(structure.getKey(), new Music(musicSound.soundEvent(), musicSound.minDelay(), musicSound.maxDelay(), musicSound.replaceCurrentMusic()));
        }
    }

    // endregion
}