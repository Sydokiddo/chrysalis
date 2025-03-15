package net.sydokiddo.chrysalis.util.technical.camera;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sydokiddo.chrysalis.Chrysalis;
import org.jetbrains.annotations.NotNull;

public record CameraShakeResetPayload(int protocolVersion) implements CustomPacketPayload {

    /**
     * The packet to reset the camera shake on the client.
     **/

    public static final Type<CameraShakeResetPayload> TYPE = new CustomPacketPayload.Type<>(Chrysalis.resourceLocationId("camera_shake_reset"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CameraShakeResetPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CameraShakeResetPayload::protocolVersion, CameraShakeResetPayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public static void handleDataOnClient(final CameraShakeResetPayload payload, final IPayloadContext context) {
        CameraShakeHandler.resetCamera();
    }
}