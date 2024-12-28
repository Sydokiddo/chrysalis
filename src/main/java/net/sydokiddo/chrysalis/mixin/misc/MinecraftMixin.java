package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import net.sydokiddo.chrysalis.misc.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.misc.util.splash_texts.CSplashManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private User user;
    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "getSplashManager", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getSplashManager(CallbackInfoReturnable<SplashManager> cir) {
        cir.setReturnValue(new CSplashManager(this.user));
    }

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getStructureMusic(CallbackInfoReturnable<Music> cir) {

        Music music = cir.getReturnValue();
        if (music == Musics.MENU) ChrysalisClient.setStructureMusic(null);

        if (music != Musics.MENU && music != Musics.CREATIVE && music != Musics.END_BOSS && music != Musics.CREDITS) {
            @Nullable Music structureMusic = ChrysalisClient.getStructureMusic();
            if (structureMusic != null) cir.setReturnValue(structureMusic);
        }
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void chrysalis$playKeyPressItemDroppingSound(CallbackInfo info) {
        if (this.player != null) EntityDataHelper.playItemDroppingSound(this.player);
    }
}