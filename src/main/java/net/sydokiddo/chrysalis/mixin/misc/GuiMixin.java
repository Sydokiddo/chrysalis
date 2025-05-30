package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomCrosshairItem;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    /**
     * Renders a custom crosshair if the player is holding an item that is an instance of the CustomCrosshairItem interface.
     **/

    @ModifyArg(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
    private ResourceLocation chrysalis$renderCustomCrosshair(ResourceLocation resourceLocation) {

        Player player = this.minecraft.player;

        if (player != null && player.getMainHandItem().getItem() instanceof CustomCrosshairItem customCrosshairItem &&
        customCrosshairItem.shouldDisplayCrosshair(player) && customCrosshairItem.getCrosshairTextureLocation() != null) return customCrosshairItem.getCrosshairTextureLocation();

        if (player != null && player.getOffhandItem().getItem() instanceof CustomCrosshairItem customCrosshairItem &&
        customCrosshairItem.shouldDisplayCrosshair(player) && customCrosshairItem.getCrosshairTextureLocation() != null) return customCrosshairItem.getCrosshairTextureLocation();

        return resourceLocation;
    }

    /**
     * Allows for the player's hunger bar to render while riding a vehicle.
     **/

    @Redirect(method = "renderFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int chrysalis$renderHungerBarWhileOnVehicle(Gui gui, LivingEntity livingEntity) {
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(GuiGraphics.class)
    public static abstract class GuiGraphicsMixin {

        @Shadow public abstract PoseStack pose();
        @Shadow public abstract void blitSprite(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int x, int y, int sizeX, int sizeY);

        /**
         * Renders a custom waxed icon in the corner of item sprites that are in the waxed_blocks tag.
         **/

        @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
        private void chrysalis$renderWaxedIcon(Font font, ItemStack itemStack, int offsetX, int offsetY, String string, CallbackInfo info) {
            if (itemStack.is(CTags.WAXED_BLOCK_ITEMS) && CConfigOptions.REWORKED_TOOLTIPS.get()) {
                this.pose().pushPose();
                this.blitSprite(RenderType::guiTexturedOverlay, Chrysalis.resourceLocationId("hud/waxed_icon"), offsetX - 1, offsetY - 1, 8, 8);
                this.pose().popPose();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(CreativeModeInventoryScreen.class)
    public static abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

        private CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title);
        }

        /**
         * Plays the generic UI click sound whenever a creative mode tab is clicked.
         **/

        @Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V"))
        private void chrysalis$playCreativeTabClickSound(double x, double y, int keyPressed, CallbackInfoReturnable<Boolean> cir) {
            EventHelper.playUIClickSound(Minecraft.getInstance());
        }

        /**
         * Plays a unique sound when an item is deleted in creative mode.
         **/

        @Unique private boolean chrysalis$playedItemDeleteSound = false;

        @Inject(method = "slotClicked", at = @At(value = "HEAD"))
        private void chrysalis$resetItemDeleteSound(Slot slot, int slotId, int mouseButton, ClickType type, CallbackInfo info) {
            this.chrysalis$playedItemDeleteSound = false;
        }

        @Inject(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleCreativeModeItemAdd(Lnet/minecraft/world/item/ItemStack;I)V", ordinal = 0))
        private void chrysalis$playDeleteAllItemsSound(Slot slot, int slotId, int mouseButton, ClickType type, CallbackInfo info) {
            if (!this.chrysalis$playedItemDeleteSound && this.minecraft != null && this.minecraft.level != null && this.minecraft.player != null && !this.minecraft.player.getInventory().isEmpty()) {
                this.chrysalis$tryPlayingSound(CSoundEvents.CREATIVE_MODE_DELETE_ALL_ITEMS, this.minecraft.level);
                this.chrysalis$playedItemDeleteSound = true;
            }
        }

        @Inject(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
        private void chrysalis$playDeleteItemSound(Slot slot, int slotId, int mouseButton, ClickType type, CallbackInfo info) {
            if (!this.menu.getCarried().isEmpty() && this.minecraft != null && this.minecraft.level != null) this.chrysalis$tryPlayingSound(CSoundEvents.CREATIVE_MODE_DELETE_ITEM, this.minecraft.level);
        }

        @Unique
        private void chrysalis$tryPlayingSound(Holder<SoundEvent> soundEvent, ClientLevel level) {
            if (CConfigOptions.CREATIVE_MODE_ITEM_DELETING_SOUNDS.get()) EventHelper.playUIClickSound(Minecraft.getInstance(), soundEvent, 0.8F + level.getRandom().nextFloat() * 0.4F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(AdvancementsScreen.class)
    public static class AdvancementsScreenMixin {

        /**
         * Plays the generic UI click sound whenever an advancement tab is clicked.
         **/

        @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientAdvancements;setSelectedTab(Lnet/minecraft/advancements/AdvancementHolder;Z)V"))
        private void chrysalis$playAdvancementTabClickSound(double x, double y, int keyPressed, CallbackInfoReturnable<Boolean> cir) {
            EventHelper.playUIClickSound(Minecraft.getInstance());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(ShulkerBoxScreen.class)
    public static abstract class ShulkerBoxScreenMixin extends AbstractContainerScreen<ShulkerBoxMenu> {

        private ShulkerBoxScreenMixin(ShulkerBoxMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title);
        }

        /**
         * Slightly lowers the inventory label on the shulker box ui to be consistent with the position of small chests.
         **/

        @Inject(method = "<init>", at = @At(value = "RETURN"))
        private void chrysalis$lowerShulkerBoxInventoryLabel(ShulkerBoxMenu menu, Inventory playerInventory, Component title, CallbackInfo info) {
            this.inventoryLabelY = this.inventoryLabelY + 1;
        }
    }
}