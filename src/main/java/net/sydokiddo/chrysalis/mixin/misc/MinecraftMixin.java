package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getStructureMusic(CallbackInfoReturnable<Music> cir) {

        Music music = cir.getReturnValue();
        if (music == Musics.MENU) ChrysalisClient.setStructureMusic(null);

        if (music != Musics.MENU && music != Musics.CREATIVE && music != Musics.END_BOSS && music != Musics.CREDITS) {
            @Nullable Music structureMusic = ChrysalisClient.getStructureMusic();
            if (structureMusic != null) cir.setReturnValue(structureMusic);
        }
    }
}