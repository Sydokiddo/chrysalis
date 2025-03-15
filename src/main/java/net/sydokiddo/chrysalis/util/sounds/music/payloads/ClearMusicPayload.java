package net.sydokiddo.chrysalis.util.sounds.music.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.sounds.music.MusicTracker;
import org.jetbrains.annotations.NotNull;

public record ClearMusicPayload(boolean shouldFade) implements CustomPacketPayload {

    /**
     * The packet to clear music on the client.
     **/

    public static final Type<ClearMusicPayload> TYPE = new CustomPacketPayload.Type<>(Chrysalis.resourceLocationId("clear_music"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearMusicPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ClearMusicPayload::shouldFade, ClearMusicPayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public static void handleDataOnClient(final ClearMusicPayload payload, final IPayloadContext context) {
        MusicTracker.clearMusicOnClient(payload.shouldFade());
    }
}