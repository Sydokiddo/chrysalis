package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class ChrysalisSoundEvents {

    // Sound Events

    public static final Holder.Reference<SoundEvent> UI_SCREENSHOT_SUCCESS = registerForHolder("ui.screenshot.success");

    // Registry

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