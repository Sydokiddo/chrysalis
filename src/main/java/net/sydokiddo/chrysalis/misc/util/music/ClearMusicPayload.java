package net.sydokiddo.chrysalis.misc.util.music;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClearMusicPayload(boolean shouldFade) implements CustomPacketPayload {

    /**
     * The packet to clear music on the client.
     **/

    public static final Type<ClearMusicPayload> TYPE = CustomPacketPayload.createType("clear_music");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearMusicPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ClearMusicPayload::shouldFade, ClearMusicPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}