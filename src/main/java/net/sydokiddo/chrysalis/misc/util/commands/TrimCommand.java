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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.*;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import java.util.Collection;

public class TrimCommand {

    private static final String
        targetsString = "targets",
        patternString = "pattern",
        materialString = "material"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register(Commands.literal("trim").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetsString, EntityArgument.entities())

            .then(Commands.literal("set")
                .then(Commands.argument(patternString, ResourceArgument.resource(commandBuildContext, Registries.TRIM_PATTERN))
                .then(Commands.argument(materialString, ResourceArgument.resource(commandBuildContext, Registries.TRIM_MATERIAL))
                .executes((commandContext) -> setArmorTrim(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString),
                ResourceArgument.getResource(commandContext, patternString, Registries.TRIM_PATTERN), ResourceArgument.getResource(commandContext, materialString, Registries.TRIM_MATERIAL), false))))
            )

            .then(Commands.literal("remove")
            .executes((commandContext) -> setArmorTrim(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), null, null, true))
            )
        ));
    }

    private static int setArmorTrim(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, Holder<TrimPattern> pattern, Holder<TrimMaterial> material, boolean removeArmorTrim) throws CommandSyntaxException {

        int returnValue = 0;

        DynamicCommandExceptionType genericFailNoItem = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.generic.fail.no_item", object));
        DynamicCommandExceptionType genericFailInvalidEntity = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.generic.fail.invalid_entity", object));

        SimpleCommandExceptionType setTrimFailGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.set_trim.fail.generic"));
        DynamicCommandExceptionType setTrimFailInvalidItem = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.set_trim.fail.invalid", object));
        DynamicCommandExceptionType setTrimFailSameType = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.set_trim.fail.same_type", object));

        SimpleCommandExceptionType removeTrimFailGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.remove_trim.fail.generic"));
        DynamicCommandExceptionType removeTrimFailNoTrim = new DynamicCommandExceptionType(object -> Component.translatable("gui.chrysalis.commands.remove_trim.fail.no_trim", object));

        for (Entity entity : entities) {

            if (entity instanceof LivingEntity livingEntity) {

                ItemStack itemStack = livingEntity.getMainHandItem();

                if (!itemStack.isEmpty()) {

                    if (removeArmorTrim) {

                        if (ItemHelper.hasArmorTrim(itemStack)) {
                            itemStack.remove(DataComponents.TRIM);
                            ++returnValue;
                            continue;
                        }

                        if (entities.size() != 1) continue;
                        throw removeTrimFailNoTrim.create(livingEntity.getName().getString());

                    } else {

                        if (itemStack.is(ItemTags.TRIMMABLE_ARMOR)) {

                            if (!ItemHelper.hasSpecificArmorTrim(itemStack, pattern, material)) {
                                itemStack.set(DataComponents.TRIM, new ArmorTrim(material, pattern));
                                ++returnValue;
                                continue;
                            }

                            if (entities.size() != 1) continue;
                            throw setTrimFailSameType.create(livingEntity.getName().getString());
                        }

                        if (entities.size() != 1) continue;
                        throw setTrimFailInvalidItem.create(livingEntity.getName().getString());
                    }
                }

                if (entities.size() != 1) continue;
                throw genericFailNoItem.create(livingEntity.getName().getString());
            }

            if (entities.size() != 1) continue;
            throw genericFailInvalidEntity.create(entity.getName().getString());
        }

        Component successSingleMessage = removeArmorTrim ? Component.translatable("gui.chrysalis.commands.remove_trim.success.single", entities.iterator().next().getDisplayName()) :
        Component.translatable("gui.chrysalis.commands.set_trim.success.single", pattern.value().description().copy().withStyle(material.value().description().getStyle()), entities.iterator().next().getDisplayName());

        Component successMultipleMessage = removeArmorTrim ? Component.translatable("gui.chrysalis.commands.remove_trim.success.multiple", entities.size()) :
        Component.translatable("gui.chrysalis.commands.set_trim.success.multiple", pattern.value().description().copy().withStyle(material.value().description().getStyle()), entities.size());

        if (returnValue == 0) {
            if (removeArmorTrim) throw removeTrimFailGeneric.create();
            else throw setTrimFailGeneric.create();
        }

        if (entities.size() == 1) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }
}