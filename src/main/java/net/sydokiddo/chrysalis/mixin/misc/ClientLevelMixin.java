package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Shadow @Final private Minecraft minecraft;

    /**
     * Renders a marker for Structure Voids in Creative Mode similarly to Barriers or Lights.
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
}