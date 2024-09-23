package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;

public class DurabilityCommand {

    private static final String
        targetsString = "targets",
        durabilityAmountString = "durability_amount",
        setRepairCostString = "set_repair_cost"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((Commands.literal("durability").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)))
        .then(Commands.argument(targetsString, EntityArgument.entities())
        .then(Commands.argument(durabilityAmountString, IntegerArgumentType.integer())
        .then(Commands.argument(setRepairCostString, IntegerArgumentType.integer(0))
        .executes((commandContext) -> changeItemDurability(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
        IntegerArgumentType.getInteger(commandContext, durabilityAmountString), IntegerArgumentType.getInteger(commandContext, setRepairCostString)))))));
    }

    private static int changeItemDurability(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int durabilityAmount, int repairCost) throws CommandSyntaxException {

        int returnValue = 0;

        SimpleCommandExceptionType failGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.durability_fail"));
        DynamicCommandExceptionType failNoItem = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability_fail_no_item", object));
        DynamicCommandExceptionType failNoDurability = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability_fail_no_durability", object));
        DynamicCommandExceptionType failInvalidEntity = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability_fail_invalid_entity", object));

        for (Entity entity : entities) {

            if (entity instanceof LivingEntity livingEntity) {

                ItemStack itemStack = livingEntity.getMainHandItem();

                if (!itemStack.isEmpty()) {

                    if (itemStack.isDamageableItem()) {

                        itemStack.setDamageValue(itemStack.getDamageValue() - durabilityAmount);
                        itemStack.set(DataComponents.REPAIR_COST, repairCost);

                        ++returnValue;
                        continue;
                    }

                    if (entities.size() != 1) continue;
                    throw failNoDurability.create(livingEntity.getName().getString());
                }

                if (entities.size() != 1) continue;
                throw failNoItem.create(livingEntity.getName().getString());
            }

            if (entities.size() != 1) continue;
            throw failInvalidEntity.create(entity.getName().getString());
        }

        if (returnValue == 0) throw failGeneric.create();
        if (entities.size() == 1) commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.durability_success_single", entities.iterator().next().getDisplayName(), durabilityAmount, repairCost), true);
        else commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.durability_success_multiple", entities.size(), durabilityAmount, repairCost), true);
        return returnValue;
    }
}