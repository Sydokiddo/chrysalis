package net.sydokiddo.chrysalis.util.technical.camera;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sydokiddo.chrysalis.Chrysalis;
import org.jetbrains.annotations.NotNull;

public record CameraShakePayload(int time, int strength, int frequency) implements CustomPacketPayload {

    /**
     * The packet to send a camera shake to the client.
     **/

    public static final Type<CameraShakePayload> TYPE = new CustomPacketPayload.Type<>(Chrysalis.resourceLocationId("camera_shake"));
    public static final StreamCodec<FriendlyByteBuf, CameraShakePayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CameraShakePayload::time, ByteBufCodecs.VAR_INT, CameraShakePayload::strength, ByteBufCodecs.VAR_INT, CameraShakePayload::frequency, CameraShakePayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public static void handleDataOnClient(final CameraShakePayload payload, final IPayloadContext context) {
        CameraShakeHandler.shakeCamera(payload.time(), payload.strength(), payload.frequency());
    }
}