package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import java.util.Objects;

public class HatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("hat").executes(HatCommand::putItemOnHead));
    }

    private static int putItemOnHead(CommandContext<CommandSourceStack> context) {

        Player player = Objects.requireNonNull(context.getSource().getPlayer());
        ItemStack heldItem = player.getMainHandItem();
        ItemStack currentHeadItem = player.getItemBySlot(EquipmentSlot.HEAD);

        Component successText = Component.translatable("gui.chrysalis.commands.hat.success", heldItem.getDisplayName(), player.getDisplayName());
        Component failNoItemText = Component.translatable("gui.chrysalis.commands.hat.fail_no_item").withStyle(ChatFormatting.RED);
        Component failInvalidItemText = Component.translatable("gui.chrysalis.commands.hat.fail_invalid_item", heldItem.getDisplayName(), player.getDisplayName()).withStyle(ChatFormatting.RED);

        if (heldItem.isEmpty()) {
            player.sendSystemMessage(failNoItemText);
        } else if (ItemHelper.getEnchantmentLevel(heldItem, Enchantments.BINDING_CURSE) > 0) {
            player.sendSystemMessage(failInvalidItemText);
        } else {

            if (!player.level().isClientSide() && !player.isSpectator() && !player.isSilent()) {

                SoundEvent soundEvent;

                if (heldItem.getItem() instanceof Equipable equipable && equipable.getEquipmentSlot() != EquipmentSlot.HEAD) soundEvent = equipable.getEquipSound().value();
                else soundEvent = SoundEvents.ARMOR_EQUIP_GENERIC.value();

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, player.getSoundSource(), 1.0F, 1.0F);
            }

            if (!currentHeadItem.isEmpty()) player.setItemSlot(EquipmentSlot.MAINHAND, currentHeadItem.copyAndClear());
            player.setItemSlot(EquipmentSlot.HEAD, heldItem.copyAndClear());
            player.sendSystemMessage(successText);
        }

        return 1;
    }
}