package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;
import java.util.HashMap;
import java.util.Map;

public class CSoundEvents {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Chrysalis.MOD_ID);

    // region Sound Events

    public static final DeferredHolder<SoundEvent, SoundEvent>

        SCREENSHOT_SUCCESS = SOUND_EVENTS.register("ui.screenshot.success", SoundEvent::createVariableRangeEvent),
        SPLASH_TEXT_SHUFFLE = SOUND_EVENTS.register("ui.splash_text.shuffle", SoundEvent::createVariableRangeEvent),
        CREATIVE_MODE_DELETE_ITEM = SOUND_EVENTS.register("ui.creative_mode.delete_item", SoundEvent::createVariableRangeEvent),
        CREATIVE_MODE_DELETE_ALL_ITEMS = SOUND_EVENTS.register("ui.creative_mode.delete_all_items", SoundEvent::createVariableRangeEvent),

        HEAL_USE = SOUND_EVENTS.register("item.heal.use", SoundEvent::createVariableRangeEvent),
        FILL_HUNGER_USE = SOUND_EVENTS.register("item.fill_hunger.use", SoundEvent::createVariableRangeEvent),
        FILL_OXYGEN_USE = SOUND_EVENTS.register("item.fill_oxygen.use", SoundEvent::createVariableRangeEvent),
        GIVE_RESISTANCE_USE = SOUND_EVENTS.register("item.give_resistance.use", SoundEvent::createVariableRangeEvent),
        CLEAR_EFFECTS_USE = SOUND_EVENTS.register("item.clear_effects.use", SoundEvent::createVariableRangeEvent),
        TELEPORT_TO_SPAWNPOINT_FAIL = SOUND_EVENTS.register("item.teleport_to_spawnpoint.fail", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_LINK = SOUND_EVENTS.register("item.aggro_wand.link", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_SELECT_TARGET_SUCCESS = SOUND_EVENTS.register("item.aggro_wand.select_target.success", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_SELECT_TARGET_FAIL = SOUND_EVENTS.register("item.aggro_wand.select_target.fail", SoundEvent::createVariableRangeEvent),
        AGGRO_WAND_REMOVE_LINKED_MOB = SOUND_EVENTS.register("item.aggro_wand.remove_linked_mob", SoundEvent::createVariableRangeEvent),
        TAME_MOB_USE = SOUND_EVENTS.register("item.tame_mob.use", SoundEvent::createVariableRangeEvent),
        RIDE_MOB_USE = SOUND_EVENTS.register("item.ride_mob.use", SoundEvent::createVariableRangeEvent),
        DRAIN_FLUIDS_USE = SOUND_EVENTS.register("item.drain_fluids.use", SoundEvent::createVariableRangeEvent),
        COPYING_SPAWN_EGG_COPY_ENTITY = SOUND_EVENTS.register("item.copying_spawn_egg.copy_entity", SoundEvent::createVariableRangeEvent),
        COPYING_SPAWN_EGG_REMOVE_COPIED_ENTITY = SOUND_EVENTS.register("item.copying_spawn_egg.remove_copied_entity", SoundEvent::createVariableRangeEvent),

        SPAWN_EGG_USE = SOUND_EVENTS.register("item.spawn_egg.use", SoundEvent::createVariableRangeEvent),
        ITEM_DROP = SOUND_EVENTS.register("entity.item.drop", SoundEvent::createVariableRangeEvent),

        ENTITY_SPAWNER_APPEAR = SOUND_EVENTS.register("entity.entity_spawner.appear", SoundEvent::createVariableRangeEvent),
        ENTITY_SPAWNER_ABOUT_TO_SPAWN_ENTITY = SOUND_EVENTS.register("entity.entity_spawner.about_to_spawn_entity", SoundEvent::createVariableRangeEvent),
        ENTITY_SPAWNER_SPAWN_ENTITY = SOUND_EVENTS.register("entity.entity_spawner.spawn_entity", SoundEvent::createVariableRangeEvent),
        ENCOUNTER_SPAWNER_SPAWN_ENTITY = SOUND_EVENTS.register("entity.encounter_spawner.spawn_entity", SoundEvent::createVariableRangeEvent),
        GENERIC_SPAWNER_CHANGE_ENTITY = SOUND_EVENTS.register("entity.generic_spawner.change_entity", SoundEvent::createVariableRangeEvent),

        EQUIP_SHIELD = SOUND_EVENTS.register("item.armor.equip_shield", SoundEvent::createVariableRangeEvent),
        EQUIP_TOTEM_OF_UNDYING = SOUND_EVENTS.register("item.armor.equip_totem_of_undying", SoundEvent::createVariableRangeEvent),
        EQUIP_CARVED_PUMPKIN = SOUND_EVENTS.register("item.armor.equip_carved_pumpkin", SoundEvent::createVariableRangeEvent),
        EQUIP_SKELETON_SKULL = SOUND_EVENTS.register("item.armor.equip_skeleton_skull", SoundEvent::createVariableRangeEvent),
        EQUIP_WITHER_SKELETON_SKULL = SOUND_EVENTS.register("item.armor.equip_wither_skeleton_skull", SoundEvent::createVariableRangeEvent),
        EQUIP_PLAYER_HEAD = SOUND_EVENTS.register("item.armor.equip_player_head", SoundEvent::createVariableRangeEvent),
        EQUIP_ZOMBIE_HEAD = SOUND_EVENTS.register("item.armor.equip_zombie_head", SoundEvent::createVariableRangeEvent),
        EQUIP_CREEPER_HEAD = SOUND_EVENTS.register("item.armor.equip_creeper_head", SoundEvent::createVariableRangeEvent),
        EQUIP_PIGLIN_HEAD = SOUND_EVENTS.register("item.armor.equip_piglin_head", SoundEvent::createVariableRangeEvent),
        EQUIP_DRAGON_HEAD = SOUND_EVENTS.register("item.armor.equip_dragon_head", SoundEvent::createVariableRangeEvent)
    ;

    // endregion

    // region Registry

    public static Map<String, Music> structures = new HashMap<>();

    public static void register(IEventBus eventBus) {

        SOUND_EVENTS.register(eventBus);

        for (Map.Entry<String, CRegistry.StructureMusicSound> structure : CRegistry.registeredStructures.entrySet()) {
            CRegistry.StructureMusicSound musicSound = structure.getValue();
            structures.put(structure.getKey(), new Music(musicSound.soundEvent(), musicSound.minDelay(), musicSound.maxDelay(), musicSound.replaceCurrentMusic()));
        }
    }

    // endregion
}