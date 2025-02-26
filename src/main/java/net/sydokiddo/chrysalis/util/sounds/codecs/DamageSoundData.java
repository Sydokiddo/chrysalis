package net.sydokiddo.chrysalis.util.sounds.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.sydokiddo.chrysalis.util.technical.CodecUtils;

public record DamageSoundData(Holder<DamageType> damageType, Holder<MobEffect> effect, Holder<SoundEvent> soundEvent, boolean forTesting) {

    /**
     * Converts information from a json file into a damage sound event for a specific damage type or status effect.
     **/

    public static final Codec<DamageSoundData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DamageType.CODEC.optionalFieldOf("damage_type", Holder.direct(null)).forGetter(DamageSoundData::damageType),
        MobEffect.CODEC.optionalFieldOf("effect", Holder.direct(null)).forGetter(DamageSoundData::effect),
        SoundEvent.CODEC.optionalFieldOf("sound_event", CodecUtils.getSoundEventHolder(SoundEvents.PLAYER_HURT.location())).forGetter(DamageSoundData::soundEvent),
        Codec.BOOL.optionalFieldOf("for_testing", false).forGetter(DamageSoundData::forTesting)
    ).apply(instance, DamageSoundData::new));
}