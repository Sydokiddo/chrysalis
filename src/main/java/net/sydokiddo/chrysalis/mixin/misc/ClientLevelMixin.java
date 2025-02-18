package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.util.helpers.WorldGenHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Shadow @Final private Minecraft minecraft;

    /**
     * Renders a marker for structure voids in creative mode similarly to barriers or lights.
     **/

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    private void chrysalis$renderStructureVoidParticle(CallbackInfoReturnable<Block> cir) {

        assert this.minecraft.gameMode != null;
        if (this.minecraft.gameMode.getPlayerMode() != GameType.CREATIVE) return;

        assert this.minecraft.player != null;
        Item item = this.minecraft.player.getMainHandItem().getItem();

        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block == Blocks.STRUCTURE_VOID) cir.setReturnValue(block);
        }
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(WeatherEffectRenderer.class)
    public static class WeatherFixMixin {

        /**
         * Fixes a vanilla bug where occasionally when going through portals while the world is loading, rain can sometimes display in dimensions where rain doesn't exist.
         **/

        @Inject(method = "render(Lnet/minecraft/world/level/Level;Lnet/minecraft/client/renderer/MultiBufferSource;IFLnet/minecraft/world/phys/Vec3;)V", at = @At(value = "HEAD"), cancellable = true)
        private void chrysalis$preventWeatherRenderingInNetherAndEnd(Level level, MultiBufferSource multiBufferSource, int int1, float float1, Vec3 vec3, CallbackInfo info) {
            if (WorldGenHelper.isNetherOrEnd(level)) info.cancel();
        }

        @Inject(method = "tickRainParticles", at = @At(value = "HEAD"), cancellable = true)
        private void chrysalis$preventWeatherTickingInNetherAndEnd(ClientLevel clientLevel, Camera camera, int int1, ParticleStatus particleStatus, CallbackInfo info) {
            if (WorldGenHelper.isNetherOrEnd(clientLevel)) info.cancel();
        }
    }
}