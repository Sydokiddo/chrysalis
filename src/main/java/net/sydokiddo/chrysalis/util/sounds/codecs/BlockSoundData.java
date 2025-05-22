package net.sydokiddo.chrysalis.util.sounds.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import java.util.Objects;
import java.util.Optional;

public record BlockSoundData(HolderSet<Block> blocks, Holder<SoundEvent> breakSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> placeSound, Holder<SoundEvent> hitSound, Holder<SoundEvent> fallSound, float volume, float pitch, String noteBlockInstrument, String enabled) {

    /**
     * Converts information from a json file into a specified block(s)'s sound group.
     **/

    public static final Codec<BlockSoundData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockSoundData::blocks),
        SoundEvent.CODEC.fieldOf("break_sound").forGetter(BlockSoundData::breakSound),
        SoundEvent.CODEC.fieldOf("step_sound").forGetter(BlockSoundData::stepSound),
        SoundEvent.CODEC.fieldOf("place_sound").forGetter(BlockSoundData::placeSound),
        SoundEvent.CODEC.fieldOf("hit_sound").forGetter(BlockSoundData::hitSound),
        SoundEvent.CODEC.fieldOf("fall_sound").forGetter(BlockSoundData::fallSound),
        Codec.FLOAT.fieldOf("volume").forGetter(BlockSoundData::volume),
        Codec.FLOAT.fieldOf("pitch").forGetter(BlockSoundData::pitch),
        Codec.STRING.optionalFieldOf("note_block_instrument", ComponentHelper.noneString).forGetter(BlockSoundData::noteBlockInstrument),
        Codec.STRING.optionalFieldOf(ComponentHelper.enabledString, ComponentHelper.trueString).forGetter(BlockSoundData::enabled)
    ).apply(instance, BlockSoundData::new));

    @SuppressWarnings("deprecation")
    public SoundType toSoundType() {
        return new SoundType(this.volume(), this.pitch(), this.breakSound().value(), this.stepSound().value(), this.placeSound().value(), this.hitSound().value(), this.fallSound().value());
    }

    public static SoundType getFinalSoundType(BlockState blockState) {
        if (Chrysalis.registryAccess == null) return null;
        Optional<BlockSoundData> optional = Chrysalis.registryAccess.lookupOrThrow(CRegistry.BLOCK_SOUND_DATA).stream().filter(codec -> codec.blocks().contains(blockState.getBlockHolder())).findFirst();
        if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) return optional.get().toSoundType();
        return null;
    }

    public static NoteBlockInstrument getNoteBlockInstrument(String string) {
        if (Objects.equals(string, ComponentHelper.noneString) || Objects.equals(string, ComponentHelper.nullString)) return null;
        NoteBlockInstrument noteBlockInstrument = StringRepresentable.fromEnum(NoteBlockInstrument::values).byName(string);
        if (noteBlockInstrument != null) return noteBlockInstrument;
        else throw new IllegalArgumentException("Invalid note block instrument '" + string + "'");
    }
}