package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisSoundEvents {

    // Sound Events

    public static final Holder.Reference<SoundEvent> UI_SCREENSHOT_SUCCESS = registerForHolder("ui.screenshot.success");

    public static final SoundEvent HEAL_USE = registerSoundEvent("item.heal.use");
    public static final SoundEvent FILL_HUNGER_USE = registerSoundEvent("item.fill_hunger.use");
    public static final SoundEvent GIVE_RESISTANCE_USE = registerSoundEvent("item.give_resistance.use");
    public static final SoundEvent CLEAR_EFFECTS_USE = registerSoundEvent("item.clear_effects.use");
    public static final SoundEvent TELEPORT_TO_SPAWNPOINT_FAIL = registerSoundEvent("item.teleport_to_spawnpoint.fail");
    public static final SoundEvent TAME_MOB_USE = registerSoundEvent("item.tame_mob.use");

    // Registry

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Chrysalis.MOD_ID, name);
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

    public static void registerSounds() {}
}