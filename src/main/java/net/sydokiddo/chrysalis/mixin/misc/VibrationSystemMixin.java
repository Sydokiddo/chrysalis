package net.sydokiddo.chrysalis.mixin.misc;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationSystem.class)
public interface VibrationSystemMixin {

    @Inject(method = "method_51383", at = @At("TAIL"))
    private static void chrysalis_addVibrationFrequencies(Object2IntOpenHashMap<net.minecraft.world.level.gameevent.GameEvent> list, CallbackInfo ci) {
        list.put(ChrysalisGameEvents.ENTITY_FLUTTER, 4);
        list.put(ChrysalisGameEvents.ENTITY_PUFF, 4);
        list.put(ChrysalisGameEvents.ENTITY_SMASH, 14);
    }
}