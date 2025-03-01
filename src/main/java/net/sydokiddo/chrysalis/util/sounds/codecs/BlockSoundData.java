package net.sydokiddo.chrysalis.util.sounds.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.sydokiddo.chrysalis.util.technical.CodecUtils;
import java.util.Objects;

public record BlockSoundData(HolderSet<Block> blocks, Holder<SoundEvent> breakSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> placeSound, Holder<SoundEvent> hitSound, Holder<SoundEvent> fallSound, float volume, float pitch, String noteBlockInstrument, boolean forTesting) {

    /**
     * Converts information from a json file into a specified block(s)'s sound group.
     **/

    public static final Codec<BlockSoundData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockSoundData::blocks),
        SoundEvent.CODEC.optionalFieldOf("break_sound", CodecUtils.getSoundEventHolder(SoundEvents.STONE_BREAK.location())).forGetter(BlockSoundData::breakSound),
        SoundEvent.CODEC.optionalFieldOf("step_sound", CodecUtils.getSoundEventHolder(SoundEvents.STONE_STEP.location())).forGetter(BlockSoundData::stepSound),
        SoundEvent.CODEC.optionalFieldOf("place_sound", CodecUtils.getSoundEventHolder(SoundEvents.STONE_PLACE.location())).forGetter(BlockSoundData::placeSound),
        SoundEvent.CODEC.optionalFieldOf("hit_sound", CodecUtils.getSoundEventHolder(SoundEvents.STONE_HIT.location())).forGetter(BlockSoundData::hitSound),
        SoundEvent.CODEC.optionalFieldOf("fall_sound", CodecUtils.getSoundEventHolder(SoundEvents.STONE_FALL.location())).forGetter(BlockSoundData::fallSound),
        Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(BlockSoundData::volume),
        Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(BlockSoundData::pitch),
        Codec.STRING.optionalFieldOf("note_block_instrument", "null").forGetter(BlockSoundData::noteBlockInstrument),
        Codec.BOOL.optionalFieldOf("for_testing", false).forGetter(BlockSoundData::forTesting)
    ).apply(instance, BlockSoundData::new));

    @SuppressWarnings("deprecation")
    public SoundType toSoundType() {
        return new SoundType(this.volume(), this.pitch(), this.breakSound().value(), this.stepSound().value(), this.placeSound().value(), this.hitSound().value(), this.fallSound().value());
    }

    public static NoteBlockInstrument getNoteBlockInstrument(String string) {
        if (Objects.equals(string, "null")) return null;
        NoteBlockInstrument noteBlockInstrument = StringRepresentable.fromEnum(NoteBlockInstrument::values).byName(string);
        if (noteBlockInstrument != null) return noteBlockInstrument;
        else throw new IllegalArgumentException("Invalid note block instrument '" + string + "'");
    }
}