package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.junebug.chrysalis.common.items.CItems;
import net.junebug.chrysalis.common.CConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultBlock.class)
public class VaultBlockMixin {

    @Mixin(VaultBlockEntity.Server.class)
    public static class VaultBlockEntityServerMixin {

        /**
         * Allows for any vault to be opened using the admin key item.
         **/

        @Inject(method = "isValidToInsert", at = @At("HEAD"), cancellable = true)
        private static void chrysalis$vaultAdminKey(VaultConfig config, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
            if (itemStack.is(CItems.ADMIN_KEY)) cir.setReturnValue(true);
        }
    }

    @Mixin(VaultConfig.class)
    public static class VaultConfigMixin {

        /**
         * Changes the default unlocking item for vaults to the generic key item.
         **/

        @ModifyArg(method = "<init>()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V"))
        private static ItemLike chrysalis$changeDefaultVaultKey(ItemLike oldValue) {
            if (CConfig.CHANGE_DEFAULT_VAULT_KEY.get()) return CItems.KEY;
            else return oldValue;
        }
    }
}