package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.util.Collection;
import java.util.Objects;

public class DisenchantCommand {

    private static final String
        targetsString = "targets",
        enchantmentString = "enchantment"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register((Commands.literal("disenchant").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))).then(Commands.argument(targetsString, EntityArgument.entities())

            .then(Commands.argument(enchantmentString, ResourceArgument.resource(commandBuildContext, Registries.ENCHANTMENT))
            .executes((commandContext) -> removeEnchantments(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), ResourceArgument.getEnchantment(commandContext, enchantmentString)))
            )

            .then(Commands.literal("*")
            .executes((commandContext) -> removeEnchantments(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), null))
            )
        ));
    }

    private static int removeEnchantments(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, Holder<Enchantment> enchantmentHolder) throws CommandSyntaxException {

        int returnValue = 0;

        SimpleCommandExceptionType failGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.disenchant.fail"));
        DynamicCommandExceptionType failNoItem = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.disenchant.fail_no_item", object));
        DynamicCommandExceptionType failNoEnchantments = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.disenchant.fail_no_enchantments", object));
        DynamicCommandExceptionType failNoSpecificEnchantment = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.disenchant.fail_no_specific_enchantment", object));
        DynamicCommandExceptionType failInvalidEntity = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.disenchant.fail_invalid_entity", object));

        for (Entity entity : entities) {

            if (entity instanceof LivingEntity livingEntity) {

                ItemStack itemStack = livingEntity.getMainHandItem();

                if (!itemStack.isEmpty()) {

                    if (itemStack.isEnchanted()) {

                        if (enchantmentHolder != null && EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, itemStack) == 0) throw failNoSpecificEnchantment.create(livingEntity.getName().getString());
                        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(itemStack, mutable -> mutable.removeIf(enchantmentHolder != null ? holder -> holder == enchantmentHolder : Objects::nonNull));

                        if (enchantmentHolder == null) {
                            itemStack.set(DataComponents.REPAIR_COST, 0);
                            if (itemStack.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) itemStack.transmuteCopy(Items.BOOK);
                        }

                        ++returnValue;
                        continue;
                    }

                    if (entities.size() != 1) continue;
                    throw failNoEnchantments.create(livingEntity.getName().getString());
                }

                if (entities.size() != 1) continue;
                throw failNoItem.create(livingEntity.getName().getString());
            }

            if (entities.size() != 1) continue;
            throw failInvalidEntity.create(entity.getName().getString());
        }

        Component singleSuccessMessage = enchantmentHolder != null ? Component.translatable("gui.chrysalis.commands.disenchant_single.success_single", Enchantment.getFullname(enchantmentHolder, 1), entities.iterator().next().getDisplayName())
        : Component.translatable("gui.chrysalis.commands.disenchant_all.success_single", entities.iterator().next().getDisplayName());

        Component multipleSuccessMessage = enchantmentHolder != null ? Component.translatable("gui.chrysalis.commands.disenchant_single.success_multiple", Enchantment.getFullname(enchantmentHolder, 1), entities.size())
        : Component.translatable("gui.chrysalis.commands.disenchant_all.success_multiple", entities.size());

        if (returnValue == 0) throw failGeneric.create();
        if (entities.size() == 1) commandSourceStack.sendSuccess(() -> singleSuccessMessage, true);
        else commandSourceStack.sendSuccess(() -> multipleSuccessMessage, true);
        return returnValue;
    }
}