package net.junebug.chrysalis.mixin.misc;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.junebug.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    /**
     * Items in the cannot_give_with_commands tag will result in a command failure when trying to give them with the /give command.
     **/

    @Inject(method = "giveItem", at = @At("HEAD"), cancellable = true)
    private static void chrysalis$preventGivingItem(CommandSourceStack commandSourceStack, ItemInput itemInput, Collection<ServerPlayer> collection, int amount, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (itemInput.createItemStack(1, false).is(CTags.CANNOT_GIVE_WITH_COMMANDS)) {
            commandSourceStack.sendFailure(Component.translatable("gui.chrysalis.commands.give.fail.invalid_item"));
            cir.setReturnValue(0);
        }
    }
}