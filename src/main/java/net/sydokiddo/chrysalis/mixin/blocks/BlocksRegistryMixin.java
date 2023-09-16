package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public class BlocksRegistryMixin {

    @Inject(at = @At("HEAD"), method = "ocelotOrParrot", cancellable = true)
    private static void chrysalis_canSpawnOnLeavesEntityTag(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(entityType.is(ChrysalisTags.CAN_SPAWN_ON_LEAVES));
    }
}