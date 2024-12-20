package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.TeleportWandItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
    private ResourceLocation chrysalis$renderDebugItemCrosshairs(ResourceLocation resourceLocation) {

        Player player = this.minecraft.player;
        Entity crosshairEntity = this.minecraft.crosshairPickEntity;

        if (player != null) {

            if (player.isHolding(ChrysalisDebugItems.TELEPORT_WAND) && TeleportWandItem.getHitResult(player).getType() == HitResult.Type.BLOCK) resourceLocation = Chrysalis.id(this.getCrosshairTexture("teleport_wand"));

            if (crosshairEntity instanceof LivingEntity livingEntity) {
                if (player.isHolding(ChrysalisDebugItems.AGGRO_WAND)) resourceLocation = Chrysalis.id(this.getCrosshairTexture("aggro_wand"));
                if (player.isHolding(ChrysalisDebugItems.TAME_MOB) && (livingEntity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame() || livingEntity instanceof AbstractHorse abstractHorse && !abstractHorse.isTamed())) resourceLocation = Chrysalis.id(this.getCrosshairTexture("tame_mob"));
                if (player.isHolding(ChrysalisDebugItems.RIDE_MOB)) resourceLocation = Chrysalis.id(this.getCrosshairTexture("ride_mob"));
            }
        }

        return resourceLocation;
    }

    @Unique
    private String getCrosshairTexture(String itemName) {
        return "hud/" + itemName + "_crosshair";
    }
}