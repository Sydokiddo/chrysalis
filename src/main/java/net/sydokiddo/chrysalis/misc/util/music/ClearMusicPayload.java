package net.sydokiddo.chrysalis.misc.util.music;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public record ClearMusicPayload(boolean clearAll, Holder<SoundEvent> soundEvent) implements CustomPacketPayload {

    public static final Type<ClearMusicPayload> TYPE = CustomPacketPayload.createType("clear_music");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearMusicPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ClearMusicPayload::clearAll, SoundEvent.STREAM_CODEC, ClearMusicPayload::soundEvent, ClearMusicPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}