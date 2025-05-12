package net.sydokiddo.chrysalis.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sydokiddo.chrysalis.util.technical.commands.util.CommandCommonMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    /**
     * Allows for the /enchant command to be able to enchant any item, even if said enchantment cannot normally be applied in survival mode.
     **/

    @ModifyExpressionValue(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;supportsEnchantment(Lnet/minecraft/core/Holder;)Z"))
    private static boolean chrysalis$allowAllEnchantments(boolean original) {
        return true;
    }

    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;isEnchantmentCompatible(Ljava/util/Collection;Lnet/minecraft/core/Holder;)Z"))
    private static boolean chrysalis$makeEnchantmentCompatible(Collection<Holder<Enchantment>> collection, Holder<Enchantment> holder) {
        return true;
    }

    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private static int chrysalis$removeEnchantmentLimit(Enchantment enchantment) {
        return 255;
    }

    /**
     * Prevents enchanting if the inputted enchantment value is lower than or equal to the enchantment value that is already on the item.
     **/

    @Inject(method = "enchant", at = @At(value = "HEAD"), cancellable = true)
    private static void chrysalis$preventEnchantingIfValueIsTooLow(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<Enchantment> holder, int enchantmentLevel, CallbackInfoReturnable<Integer> cir) {
        for (Entity entity : collection) {
            if (entity instanceof LivingEntity livingEntity && enchantmentLevel <= EnchantmentHelper.getTagEnchantmentLevel(holder, livingEntity.getMainHandItem())) {
                CommandCommonMethods.sendFeedbackMessage(false, commandSourceStack.getPlayer(), Component.translatable("gui.chrysalis.commands.enchant.fail.low_level").withStyle(ChatFormatting.RED));
                cir.setReturnValue(0);
            }
        }
    }

    /**
     * Reworks the /enchant command to allow for 'max' to be inserted in place of the level, which enchants the item with the maximum level of the given enchantment.
     **/

    @Unique
    private static final String
        chrysalis$targetsString = "targets",
        chrysalis$enchantmentString = "enchantment",
        chrysalis$levelString = "level"
    ;

    @Inject(method = "register", at = @At(value = "HEAD"), cancellable = true)
    private static void chrysalis$changeEnchantCommand(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, CallbackInfo info) {

        info.cancel();

        commandDispatcher.register(Commands.literal("enchant").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(chrysalis$targetsString, EntityArgument.entities())

            .then((Commands.argument(chrysalis$enchantmentString, ResourceArgument.resource(commandBuildContext, Registries.ENCHANTMENT))
                .executes((commandContext) -> EnchantCommand.enchant(commandContext.getSource(), EntityArgument.getEntities(commandContext, chrysalis$targetsString), ResourceArgument.getEnchantment(commandContext, chrysalis$enchantmentString), 1))
            )

            .then(Commands.argument(chrysalis$levelString, IntegerArgumentType.integer(0))
                .executes((commandContext) -> EnchantCommand.enchant(commandContext.getSource(), EntityArgument.getEntities(commandContext, chrysalis$targetsString), ResourceArgument.getEnchantment(commandContext, chrysalis$enchantmentString), IntegerArgumentType.getInteger(commandContext, chrysalis$levelString)))
            )

            .then(Commands.literal("max")
                .executes((commandContext) -> EnchantCommand.enchant(commandContext.getSource(), EntityArgument.getEntities(commandContext, chrysalis$targetsString), ResourceArgument.getEnchantment(commandContext, chrysalis$enchantmentString), ResourceArgument.getEnchantment(commandContext, chrysalis$enchantmentString).value().getMaxLevel()))
            ))
        ));
    }
}