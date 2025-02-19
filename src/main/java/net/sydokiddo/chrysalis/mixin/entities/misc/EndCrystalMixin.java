package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystal.class)
public class EndCrystalMixin {

    /**
     * End Crystals will not explode when receiving any damage type that is in the does_not_explode_end_crystals tag.
     **/

    @Redirect(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean chrysalis$doesNotExplodeEndCrystalsTag(DamageSource damageSource, TagKey<DamageType> tagKey) {
        return damageSource.is(ChrysalisTags.DOES_NOT_EXPLODE_END_CRYSTALS);
    }
}