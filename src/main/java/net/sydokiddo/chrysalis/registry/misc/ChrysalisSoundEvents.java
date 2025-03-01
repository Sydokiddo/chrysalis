package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import java.util.HashMap;
import java.util.Map;

public class ChrysalisSoundEvents {

    // region Sound Events

    public static final Holder.Reference<SoundEvent> SCREENSHOT_SUCCESS = registerForHolder("ui.screenshot.success");
    public static final Holder.Reference<SoundEvent> SPLASH_TEXT_SHUFFLE = registerForHolder("ui.splash_text.shuffle");
    public static final Holder.Reference<SoundEvent> ENDERMAN_ENCOUNTER_MUSIC = registerForHolder("music.encounter.enderman");

    public static final SoundEvent HEAL_USE = registerSoundEvent("item.heal.use");
    public static final SoundEvent FILL_HUNGER_USE = registerSoundEvent("item.fill_hunger.use");
    public static final SoundEvent FILL_OXYGEN_USE = registerSoundEvent("item.fill_oxygen.use");
    public static final SoundEvent GIVE_RESISTANCE_USE = registerSoundEvent("item.give_resistance.use");
    public static final SoundEvent CLEAR_EFFECTS_USE = registerSoundEvent("item.clear_effects.use");
    public static final SoundEvent TELEPORT_TO_SPAWNPOINT_FAIL = registerSoundEvent("item.teleport_to_spawnpoint.fail");
    public static final SoundEvent TAME_MOB_USE = registerSoundEvent("item.tame_mob.use");
    public static final SoundEvent RIDE_MOB_USE = registerSoundEvent("item.ride_mob.use");
    public static final SoundEvent AGGRO_WAND_LINK = registerSoundEvent("item.aggro_wand.link");
    public static final SoundEvent AGGRO_WAND_SELECT_TARGET_FAIL = registerSoundEvent("item.aggro_wand.select_target.fail");
    public static final SoundEvent AGGRO_WAND_SELECT_TARGET_SUCCESS = registerSoundEvent("item.aggro_wand.select_target.success");

    public static final SoundEvent ITEM_DROP = registerSoundEvent("entity.item.drop");

    // endregion

    // region Registry

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = ChrysalisMod.id(name);
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, soundEvent);
    }

    private static Holder.Reference<SoundEvent> registerForHolder(String name) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, ChrysalisMod.id(name), SoundEvent.createVariableRangeEvent(ChrysalisMod.id(name)));
    }

    public static Map<String, Music> structures = new HashMap<>();

    public static void registerStructureMusic() {
        for (Map.Entry<String, ChrysalisRegistry.StructureMusicSound> structure : ChrysalisRegistry.registeredStructures.entrySet()) {
            ChrysalisRegistry.StructureMusicSound musicSound = structure.getValue();
            structures.put(structure.getKey(), new Music(musicSound.soundEvent(), musicSound.minDelay(), musicSound.maxDelay(), musicSound.replaceCurrentMusic()));
        }
    }

    public static void registerSounds() {}

    // endregion
}