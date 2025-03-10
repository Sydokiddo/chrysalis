package net.sydokiddo.chrysalis.util.sounds.music;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sydokiddo.chrysalis.registry.ChrysalisClientRegistry;
import org.jetbrains.annotations.NotNull;

public record ResetMusicFadePayload(int protocolVersion) implements CustomPacketPayload {

    /**
     * The packet to reset the music tracker's volume fading on the client.
     **/

    public static final Type<ResetMusicFadePayload> TYPE = CustomPacketPayload.createType("reset_music_fade");
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetMusicFadePayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ResetMusicFadePayload::protocolVersion, ResetMusicFadePayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public static void handleDataOnClient(final ResetMusicFadePayload payload, final IPayloadContext context) {
        ChrysalisClientRegistry.resetMusicFade = true;
    }
}