package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("unused")
public class ConfigHelper {

    /**
     * Various methods to assist with creating generic config categories and options when paired with a config library mod.
     **/

    // region Config Option Components

    public static Component genericCategoryName(String modID) {
        MutableComponent icon = ChrysalisRegistry.GEAR_ICON;
        ItemHelper.setTooltipIconsFont(icon, Chrysalis.MOD_ID);
        return Component.translatable("gui.chrysalis.config_category.generic", icon, Component.translatable("mod." + modID));
    }

    public static Component categoryName(String string) {
        MutableComponent icon = ChrysalisRegistry.GEAR_ICON;
        ItemHelper.setTooltipIconsFont(icon, Chrysalis.MOD_ID);
        return Component.translatable(string, icon, icon);
    }

    public static Component groupName(String modID, String translationString, MutableComponent icon) {
        return groupNameWithCustomFont(modID, "config_icons", translationString, icon);
    }

    public static Component groupNameWithCustomFont(String modID, String fontName, String translationString, MutableComponent icon) {
        icon.setStyle(icon.getStyle().withFont(ResourceLocation.fromNamespaceAndPath(modID, fontName)));
        return Component.translatable(translationString, icon, icon);
    }

    private static String optionTranslationString(String modID, String optionName) {
        return "gui." + modID + ".config." + optionName;
    }

    public static Component optionName(String modID, String optionName) {
        return Component.translatable(optionTranslationString(modID, optionName));
    }

    public static Component optionDescription(String modID, String optionName) {
        return Component.translatable(optionTranslationString(modID, optionName) + descriptionString);
    }

    // endregion

    // region Config Option Math

    public static int roundedConfigNumber(float value) {
        return new BigDecimal(value).setScale(1, RoundingMode.UP).intValueExact();
    }

    public static Component booleanComponent(boolean value) {
        return value ? Component.translatable("gui.yes") : Component.translatable("gui.no");
    }

    public static Component percentageComponent(float value) {
        return Component.literal(roundedConfigNumber(value * 100.0F) + "%");
    }

    public static Component ticksComponent(int value) {
        return value > 1 ? Component.translatable("gui.chrysalis.config_controller.ticks_plural", value) : Component.translatable("gui.chrysalis.config_controller.ticks_singular", value);
    }

    public static Component blocksComponent(float value) {
        return value > 1 ? Component.translatable("gui.chrysalis.config_controller.blocks_plural", roundedConfigNumber(value)) : Component.translatable("gui.chrysalis.config_controller.blocks_singular", roundedConfigNumber(value));
    }

    // endregion

    // region Category Name Translation Strings

    public static final String

        categoryString = "gui.chrysalis.config_category.",
        descriptionString = ".description",

        genericCategoryName = categoryString + "generic",
        genericCategoryDescription = genericCategoryName + descriptionString,

        vanillaMobsCategoryName = categoryString + "vanilla_mobs",
        vanillaMobsCategoryDescription = vanillaMobsCategoryName + descriptionString,

        modMobsCategoryName = categoryString + "mod_mobs",
        modMobsCategoryDescription = modMobsCategoryName + descriptionString,

        mobsCategoryName = categoryString + "mobs",
        mobsCategoryDescription = mobsCategoryName + descriptionString,

        blocksAndItemsCategoryName = categoryString + "blocks_and_items",
        blocksAndItemsCategoryDescription = blocksAndItemsCategoryName + descriptionString,

        blocksCategoryName = categoryString + "blocks",
        blocksCategoryDescription = blocksCategoryName + descriptionString,

        itemsCategoryName = categoryString + "items",
        itemsCategoryDescription = itemsCategoryName + descriptionString,

        potionsCategoryName = categoryString + "potions",
        potionsCategoryDescription = potionsCategoryName + descriptionString,

        armorCategoryName = categoryString + "armor",
        armorCategoryDescription = armorCategoryName + descriptionString,

        enchantmentsCategoryName = categoryString + "enchantments",
        enchantmentsCategoryDescription = enchantmentsCategoryName + descriptionString,

        tooltipsCategoryName = categoryString + "tooltips",
        tooltipsCategoryDescription = tooltipsCategoryName + descriptionString,

        miscellaneousCategoryName = categoryString + "miscellaneous",
        miscellaneousCategoryDescription = miscellaneousCategoryName + descriptionString,

        experimentalCategoryName = categoryString + "experimental",
        experimentalCategoryDescription = experimentalCategoryName + descriptionString
    ;

    // endregion
}