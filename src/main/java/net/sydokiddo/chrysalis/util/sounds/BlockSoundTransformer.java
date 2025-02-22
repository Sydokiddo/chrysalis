package net.sydokiddo.chrysalis.util.sounds;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public record BlockSoundTransformer(HolderSet<Block> blocks, Holder<SoundEvent> breakSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> placeSound, Holder<SoundEvent> hitSound, Holder<SoundEvent> fallSound, float volume, float pitch, String noteBlockInstrument) {

    /**
     * Converts information from a json file into a specified block(s)'s sound group.
     **/

    public static final Codec<BlockSoundTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockSoundTransformer::blocks),
        SoundEvent.CODEC.optionalFieldOf("break_sound", BlockSoundTransformer.forHolder(SoundEvents.STONE_BREAK.location())).forGetter(BlockSoundTransformer::breakSound),
        SoundEvent.CODEC.optionalFieldOf("step_sound", BlockSoundTransformer.forHolder(SoundEvents.STONE_STEP.location())).forGetter(BlockSoundTransformer::stepSound),
        SoundEvent.CODEC.optionalFieldOf("place_sound", BlockSoundTransformer.forHolder(SoundEvents.STONE_PLACE.location())).forGetter(BlockSoundTransformer::placeSound),
        SoundEvent.CODEC.optionalFieldOf("hit_sound", BlockSoundTransformer.forHolder(SoundEvents.STONE_HIT.location())).forGetter(BlockSoundTransformer::hitSound),
        SoundEvent.CODEC.optionalFieldOf("fall_sound", BlockSoundTransformer.forHolder(SoundEvents.STONE_FALL.location())).forGetter(BlockSoundTransformer::fallSound),
        Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(BlockSoundTransformer::volume),
        Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(BlockSoundTransformer::pitch),
        Codec.STRING.optionalFieldOf("note_block_instrument", "harp").forGetter(BlockSoundTransformer::noteBlockInstrument)
    ).apply(instance, BlockSoundTransformer::new));

    private static Holder<SoundEvent> forHolder(ResourceLocation resourceLocation) {
        return Holder.direct(SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public SoundType toSoundType() {
        return new SoundType(this.volume(), this.pitch(), this.breakSound().value(), this.stepSound().value(), this.placeSound().value(), this.hitSound().value(), this.fallSound().value());
    }

    public static NoteBlockInstrument getNoteBlockInstrument(String string) {
        NoteBlockInstrument noteBlockInstrument = StringRepresentable.fromEnum(NoteBlockInstrument::values).byName(string);
        if (noteBlockInstrument != null) return noteBlockInstrument;
        else throw new IllegalArgumentException("Invalid note block instrument '" + string + "'");
    }
}