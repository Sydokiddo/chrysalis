package net.sydokiddo.chrysalis.util.sounds.music;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public record QueuedMusicPayload(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) implements CustomPacketPayload {

    /**
     * The packet to send a queued music track to the client.
     **/

    public static final Type<QueuedMusicPayload> TYPE = CustomPacketPayload.createType("queued_music");
    public static final StreamCodec<RegistryFriendlyByteBuf, QueuedMusicPayload> CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, QueuedMusicPayload::soundEvent, ByteBufCodecs.VAR_INT, QueuedMusicPayload::minDelay, ByteBufCodecs.VAR_INT, QueuedMusicPayload::maxDelay, ByteBufCodecs.BOOL, QueuedMusicPayload::replaceCurrentMusic, QueuedMusicPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}