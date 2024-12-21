package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.Equippable;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import java.util.Objects;

public class HatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("hat").executes(HatCommand::putItemOnHead));
    }

    private static int putItemOnHead(CommandContext<CommandSourceStack> context) {

        ServerPlayer serverPlayer = Objects.requireNonNull(context.getSource().getPlayer());
        ItemStack heldItem = serverPlayer.getMainHandItem();
        ItemStack currentHeadItem = serverPlayer.getItemBySlot(EquipmentSlot.HEAD);

        Component successText = Component.translatable("gui.chrysalis.commands.hat.success", heldItem.getDisplayName(), serverPlayer.getDisplayName());
        Component failNoItemText = Component.translatable("gui.chrysalis.commands.hat.fail.no_item").withStyle(ChatFormatting.RED);
        Component failInvalidItemText = Component.translatable("gui.chrysalis.commands.hat.fail.invalid_item", heldItem.getDisplayName(), serverPlayer.getDisplayName()).withStyle(ChatFormatting.RED);

        if (heldItem.isEmpty()) {
            serverPlayer.sendSystemMessage(failNoItemText);
        } else if (ItemHelper.getEnchantmentLevel(heldItem, Enchantments.BINDING_CURSE) > 0) {
            serverPlayer.sendSystemMessage(failInvalidItemText);
        } else {

            if (!serverPlayer.level().isClientSide() && !serverPlayer.isSpectator() && !serverPlayer.isSilent()) {

                SoundEvent soundEvent;

                DataComponentType<Equippable> equippableComponent = DataComponents.EQUIPPABLE;
                Equippable getEquippableComponent = heldItem.getComponents().get(equippableComponent);

                if (heldItem.has(equippableComponent) && getEquippableComponent != null) {
                    if (getEquippableComponent.slot() == EquipmentSlot.HEAD) soundEvent = SoundEvents.EMPTY;
                    else soundEvent = getEquippableComponent.equipSound().value();
                } else {
                    soundEvent = SoundEvents.ARMOR_EQUIP_GENERIC.value();
                }

                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), soundEvent, serverPlayer.getSoundSource(), 1.0F, 1.0F);
            }

            if (!currentHeadItem.isEmpty()) serverPlayer.setItemSlot(EquipmentSlot.MAINHAND, currentHeadItem.copyAndClear());
            serverPlayer.setItemSlot(EquipmentSlot.HEAD, heldItem.copyAndClear());
            serverPlayer.sendSystemMessage(successText);
        }

        return 1;
    }
}