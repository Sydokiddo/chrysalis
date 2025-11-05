package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.junebug.chrysalis.common.misc.CGameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin extends Block {

    private BaseFireBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Prevents nether portals from activating if the doNetherPortalActivating game rule is set to false.
     **/

    @Inject(at = @At("HEAD"), method = "inPortalDimension", cancellable = true)
    private static void chrysalis$preventNetherPortalActivating(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(CGameRules.RULE_DO_NETHER_PORTAL_ACTIVATING)) cir.setReturnValue(false);
    }

    /**
     * Middle-clicking on a fire block gives flint and steel.
     **/

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean includeData) {
        return Items.FLINT_AND_STEEL.getDefaultInstance();
    }
}