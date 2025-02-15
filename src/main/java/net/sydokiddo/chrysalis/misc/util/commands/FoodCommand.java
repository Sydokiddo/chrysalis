package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class FoodCommand {

    /**
     * A command to configure a player's nutrition and saturation values.
     **/

    private static final String
        targetString = "target",
        nutritionAmountString = "nutrition_amount",
        saturationAmountString = "saturation_amount"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("food").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetString, EntityArgument.player())

            .then(Commands.literal("add")
                .then(Commands.argument(nutritionAmountString, IntegerArgumentType.integer(1))
                .then(Commands.argument(saturationAmountString, FloatArgumentType.floatArg(1))
                .executes((commandContext) -> addHungerPoints(commandContext.getSource(), EntityArgument.getPlayer(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, nutritionAmountString), FloatArgumentType.getFloat(commandContext, saturationAmountString)))))
            )

            .then(Commands.literal("remove")
                .then(Commands.argument(nutritionAmountString, IntegerArgumentType.integer(1))
                .then(Commands.argument(saturationAmountString, FloatArgumentType.floatArg(1))
                .executes((commandContext) -> removeHungerPoints(commandContext.getSource(), EntityArgument.getPlayer(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, nutritionAmountString), FloatArgumentType.getFloat(commandContext, saturationAmountString)))))
            )

            .then(Commands.literal("set")
                .then(Commands.argument(nutritionAmountString, IntegerArgumentType.integer())
                .then(Commands.argument(saturationAmountString, FloatArgumentType.floatArg())
                .executes((commandContext) -> setHungerPoints(commandContext.getSource(), EntityArgument.getPlayer(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, nutritionAmountString), FloatArgumentType.getFloat(commandContext, saturationAmountString)))))
            )
        ));
    }

    private static int addHungerPoints(CommandSourceStack commandSourceStack, Player player, int nutritionAmount, float saturationAmount) {
        FoodData foodData = player.getFoodData();
        adjustHungerPoints(player, foodData.getFoodLevel() + nutritionAmount, foodData.getSaturationLevel() + saturationAmount);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.food.add", nutritionAmount, saturationAmount, player.getDisplayName()), true);
        return 1;
    }

    private static int removeHungerPoints(CommandSourceStack commandSourceStack, Player player, int nutritionAmount, float saturationAmount) {
        FoodData foodData = player.getFoodData();
        adjustHungerPoints(player, foodData.getFoodLevel() - nutritionAmount, foodData.getSaturationLevel() - saturationAmount);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.food.remove", nutritionAmount, saturationAmount, player.getDisplayName()), true);
        return 1;
    }

    private static int setHungerPoints(CommandSourceStack commandSourceStack, Player player, int nutritionAmount, float saturationAmount) {
        adjustHungerPoints(player, nutritionAmount, saturationAmount);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.food.set", nutritionAmount, saturationAmount, player.getDisplayName()), true);
        return 1;
    }

    private static void adjustHungerPoints(Player player, int nutritionAmount, float saturationAmount) {
        player.getFoodData().setFoodLevel(nutritionAmount);
        player.getFoodData().setSaturation(saturationAmount);
    }
}