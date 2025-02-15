package net.sydokiddo.chrysalis.misc.util.music;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ResetMusicFadePayload(int protocolVersion) implements CustomPacketPayload {

    /**
     * The packet to reset the music tracker's volume fading on the client.
     **/

    public static final Type<ResetMusicFadePayload> TYPE = CustomPacketPayload.createType("reset_music_fade");
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetMusicFadePayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ResetMusicFadePayload::protocolVersion, ResetMusicFadePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}