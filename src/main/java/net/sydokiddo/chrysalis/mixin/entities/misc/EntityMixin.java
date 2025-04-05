package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.*;
import net.sydokiddo.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique private Entity chrysalis$entity = (Entity) (Object) this;
    @Shadow public abstract boolean isAlive();

    /**
     * Allows for items in the increased_pick_radius tag to be able to select entities within a wider range when far away from them.
     **/

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "getPickRadius", at = @At("RETURN"), cancellable = true)
    private void chrysalis$increasedPickRadius(CallbackInfoReturnable<Float> cir) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        ItemStack mainHandItem = minecraft.player.getMainHandItem();
        ItemStack offHandItem = minecraft.player.getOffhandItem();

        if (mainHandItem.getItem() instanceof KillWandItem && !KillWandItem.canAttack(this.chrysalis$entity, minecraft.player)) return;
        if (mainHandItem.has(CDataComponents.INCREASED_PICK_RADIUS) || offHandItem.has(CDataComponents.INCREASED_PICK_RADIUS)) cir.setReturnValue(minecraft.player.distanceTo(this.chrysalis$entity) > 8 ? 0.5F : 0.0F);
    }

    /**
     * Prevents entities from rendering entirely while under the invisibility 2 effect or higher.
     **/

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventEntityRendering(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (this.chrysalis$entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(invisibility) && Objects.requireNonNull(livingEntity.getEffect(invisibility)).getAmplifier() > 0) cir.setReturnValue(false);
    }

    /**
     * Allows for various debug utility items to be used on entities with a higher priority than other interactions.
     **/

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void chrysalis$debugItemInteractions(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!this.isAlive()) return;
        ItemStack itemStack = player.getItemInHand(interactionHand);

        // Non-Living Entity Interactions

        if (itemStack.getItem() instanceof CopyingSpawnEggItem copyingSpawnEggItem) cir.setReturnValue(CopyingSpawnEggItem.copyEntity(copyingSpawnEggItem, itemStack, player, this.chrysalis$entity, interactionHand));

        // Living Entity Interactions

        if (!(this.chrysalis$entity instanceof LivingEntity livingEntity)) return;
        if (itemStack.getItem() instanceof AggroWandItem) cir.setReturnValue(AggroWandItem.doInteraction(itemStack, player, livingEntity, interactionHand));
        if (itemStack.getItem() instanceof TameMobItem) cir.setReturnValue(TameMobItem.doInteraction(itemStack, player, livingEntity, interactionHand));
        if (itemStack.getItem() instanceof RideMobItem) cir.setReturnValue(RideMobItem.doInteraction(itemStack, player, livingEntity, interactionHand));
    }

    @Mixin(EntityType.class)
    public static abstract class EntityTypeMixin {

        @Shadow public abstract boolean is(TagKey<EntityType<?>> tagKey);

        /**
         * Any entity in the hidden_from_summon_command tag will not be able to be summoned with the /summon command.
         **/

        @Inject(method = "canSummon", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideEntityFromSummonCommand(CallbackInfoReturnable<Boolean> cir) {
            if (!Chrysalis.IS_DEBUG && this.is(CTags.HIDDEN_FROM_SUMMON_COMMAND)) cir.setReturnValue(false);
        }
    }
}