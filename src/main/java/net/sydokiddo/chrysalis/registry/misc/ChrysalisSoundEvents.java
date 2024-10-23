package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import java.util.HashMap;
import java.util.Map;

public class ChrysalisSoundEvents {

    // Sound Events

    public static final Holder.Reference<SoundEvent> UI_SCREENSHOT_SUCCESS = registerForHolder("ui.screenshot.success");

    public static final SoundEvent HEAL_USE = registerSoundEvent("item.heal.use");
    public static final SoundEvent FILL_HUNGER_USE = registerSoundEvent("item.fill_hunger.use");
    public static final SoundEvent GIVE_RESISTANCE_USE = registerSoundEvent("item.give_resistance.use");
    public static final SoundEvent CLEAR_EFFECTS_USE = registerSoundEvent("item.clear_effects.use");
    public static final SoundEvent TELEPORT_TO_SPAWNPOINT_FAIL = registerSoundEvent("item.teleport_to_spawnpoint.fail");
    public static final SoundEvent TAME_MOB_USE = registerSoundEvent("item.tame_mob.use");
    public static final SoundEvent RIDE_MOB_USE = registerSoundEvent("item.ride_mob.use");

    // Registry

    // region Base Registries

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = Chrysalis.id(name);
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, soundEvent);
    }

    @SuppressWarnings("all")
    private static Holder.Reference<SoundEvent> registerForHolder(String name) {
        return registerForHolder(Chrysalis.id(name));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(ResourceLocation resourceLocation) {
        return registerForHolder(resourceLocation, resourceLocation);
    }

    private static Holder.Reference<SoundEvent> registerForHolder(ResourceLocation resourceLocation, ResourceLocation secondaryResourceLocation) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(secondaryResourceLocation));
    }

    // endregion

    // region Structure Music Registry

    public static Map<String, Music> structures = new HashMap<>();

    public static Music registerMusic(String name, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        ResourceLocation resourceLocation = ResourceLocation.parse(name);
        SoundEvent music = SoundEvent.createVariableRangeEvent(resourceLocation);
        return new Music(registerForHolder(resourceLocation, music.location()), minDelay, maxDelay, replaceCurrentMusic);
    }

    public static void registerStructureMusic() {
        for (Map.Entry<String, ChrysalisRegistry.StructureMusicSound> structure : ChrysalisRegistry.registeredStructures.entrySet()) {
            ChrysalisRegistry.StructureMusicSound musicSound = structure.getValue();
            structures.put(structure.getKey(), registerMusic(musicSound.name(), musicSound.minDelay(), musicSound.maxDelay(), musicSound.replaceCurrentMusic()));
        }
    }

    // endregion

    public static void registerSounds() {}
}