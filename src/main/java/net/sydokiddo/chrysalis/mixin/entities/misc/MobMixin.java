package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    private MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Mobs being able to pick up items is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$mobsPickingUpItemsPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisRegistry.RULE_PASSIVE_GRIEFING;
    }

    /**
     * Dealing more than 30 damage to any mob in the has_damage_cap tag will set the damage back to 30.
     **/

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damageAmount) {

        float damageToDeal = damageAmount;
        float damageCap = 30;

        if (this.getType().is(ChrysalisTags.HAS_DAMAGE_CAP) && damageAmount > damageCap && damageAmount < Float.MAX_VALUE && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (Chrysalis.IS_DEBUG && !this.level().isClientSide()) Chrysalis.LOGGER.info("{} has taken damage higher than {}, setting damage amount to {}", this.getName().getString(), damageCap, damageCap);
            damageToDeal = damageCap;
        }

        return super.hurtServer(serverLevel, damageSource, damageToDeal);
    }
}