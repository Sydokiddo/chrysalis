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
import net.minecraft.world.item.Items;
import java.util.Collection;

public class DurabilityCommand {

    private static final String
        durabilityString = "durability",
        durabilityAmountString = "durability_amount",
        repairCostString = "repair_cost",
        repairCostAmountString = "repair_cost_amount",
        targetsString = "targets"
    ;

    private static final SimpleCommandExceptionType failGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.durability.fail"));

    private static final DynamicCommandExceptionType
        failNoDurability = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability.fail_no_durability", object)),
        failNoItem = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability.fail_no_item", object)),
        failInvalidEntity = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.durability.fail_invalid_entity", object))
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((Commands.literal(durabilityString).requires((commandSourceStack) -> commandSourceStack.hasPermission(2))).then(Commands.argument(targetsString, EntityArgument.entities())

        .then(Commands.literal("add")

            .then(Commands.literal(durabilityString)
            .then(Commands.argument(durabilityAmountString, IntegerArgumentType.integer(1))
            .executes((commandContext) -> addDurability(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, durabilityAmountString)))))

            .then(Commands.literal(repairCostString)
            .then(Commands.argument(repairCostAmountString, IntegerArgumentType.integer(1))
            .executes((commandContext) -> addRepairCost(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, repairCostAmountString)))))
        )

        .then(Commands.literal("remove")

            .then(Commands.literal(durabilityString)
            .then(Commands.argument(durabilityAmountString, IntegerArgumentType.integer(1))
            .executes((commandContext) -> removeDurability(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, durabilityAmountString)))))

            .then(Commands.literal(repairCostString)
            .then(Commands.argument(repairCostAmountString, IntegerArgumentType.integer(1))
            .executes((commandContext) -> removeRepairCost(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, repairCostAmountString)))))
        )

        .then(Commands.literal("set")

            .then(Commands.literal(durabilityString)
            .then(Commands.argument(durabilityAmountString, IntegerArgumentType.integer())
            .executes((commandContext) -> setDurability(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, durabilityAmountString)))))

            .then(Commands.literal(repairCostString)
            .then(Commands.argument(repairCostAmountString, IntegerArgumentType.integer(0))
            .executes((commandContext) -> setRepairCost(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
            IntegerArgumentType.getInteger(commandContext, repairCostAmountString)))))
        )
        ));
    }

    private static int changeItemProperties(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int amount, boolean setRepairCost, Component successSingleMessage, Component successMultipleMessage) throws CommandSyntaxException {

        int returnValue = 0;

        for (Entity entity : entities) {

            if (entity instanceof LivingEntity livingEntity) {

                ItemStack itemStack = livingEntity.getMainHandItem();

                if (!itemStack.isEmpty()) {

                    if (itemStack.isDamageableItem()) {
                        if (setRepairCost) itemStack.set(DataComponents.REPAIR_COST, amount);
                        else itemStack.set(DataComponents.DAMAGE, amount);
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
        if (entities.size() == 1) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }

    // region Durability

    private static int addDurability(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int durabilityAmount) throws CommandSyntaxException {

        ItemStack itemStack = new ItemStack(Items.AIR);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) itemStack = livingEntity.getMainHandItem();
            else throw failInvalidEntity.create(entity.getName().getString());
        }

        return changeItemProperties(commandSourceStack, entities, itemStack.getDamageValue() - durabilityAmount, false,
        Component.translatable("gui.chrysalis.commands.add_durability.success_single", entities.iterator().next().getDisplayName(), durabilityAmount),
        Component.translatable("gui.chrysalis.commands.add_durability.success_multiple", entities.size(), durabilityAmount));
    }

    private static int removeDurability(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int durabilityAmount) throws CommandSyntaxException {

        ItemStack itemStack = new ItemStack(Items.AIR);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) itemStack = livingEntity.getMainHandItem();
            else throw failInvalidEntity.create(entity.getName().getString());
        }

        return changeItemProperties(commandSourceStack, entities, itemStack.getDamageValue() + durabilityAmount, false,
        Component.translatable("gui.chrysalis.commands.remove_durability.success_single", entities.iterator().next().getDisplayName(), durabilityAmount),
        Component.translatable("gui.chrysalis.commands.remove_durability.success_multiple", entities.size(), durabilityAmount));
    }

    private static int setDurability(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int durabilityAmount) throws CommandSyntaxException {

        ItemStack itemStack = new ItemStack(Items.AIR);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) itemStack = livingEntity.getMainHandItem();
            else throw failInvalidEntity.create(entity.getName().getString());
        }

        return changeItemProperties(commandSourceStack, entities, itemStack.getMaxDamage() - durabilityAmount, false,
        Component.translatable("gui.chrysalis.commands.set_durability.success_single", entities.iterator().next().getDisplayName(), durabilityAmount),
        Component.translatable("gui.chrysalis.commands.set_durability.success_multiple", entities.size(), durabilityAmount));
    }

    // endregion

    // region Repair Cost

    private static int addRepairCost(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int repairCostAmount) throws CommandSyntaxException {

        ItemStack itemStack = new ItemStack(Items.AIR);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) itemStack = livingEntity.getMainHandItem();
            else throw failInvalidEntity.create(entity.getName().getString());
        }

        return changeItemProperties(commandSourceStack, entities, itemStack.getOrDefault(DataComponents.REPAIR_COST, 0) + repairCostAmount, true,
        Component.translatable("gui.chrysalis.commands.add_repair_cost.success_single", repairCostAmount, entities.iterator().next().getDisplayName()),
        Component.translatable("gui.chrysalis.commands.add_repair_cost.success_multiple", repairCostAmount, entities.size()));
    }

    private static int removeRepairCost(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int repairCostAmount) throws CommandSyntaxException {

        ItemStack itemStack = new ItemStack(Items.AIR);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) itemStack = livingEntity.getMainHandItem();
            else throw failInvalidEntity.create(entity.getName().getString());
        }

        return changeItemProperties(commandSourceStack, entities, itemStack.getOrDefault(DataComponents.REPAIR_COST, 0) - repairCostAmount, true,
        Component.translatable("gui.chrysalis.commands.remove_repair_cost.success_single", repairCostAmount, entities.iterator().next().getDisplayName()),
        Component.translatable("gui.chrysalis.commands.remove_repair_cost.success_multiple", repairCostAmount, entities.size()));
    }

    private static int setRepairCost(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int repairCostAmount) throws CommandSyntaxException {
        return changeItemProperties(commandSourceStack, entities, repairCostAmount, true,
        Component.translatable("gui.chrysalis.commands.set_repair_cost.success_single", entities.iterator().next().getDisplayName(), repairCostAmount),
        Component.translatable("gui.chrysalis.commands.set_repair_cost.success_multiple", entities.size(), repairCostAmount));
    }

    // endregion
}