package net.sydokiddo.chrysalis.util.technical.camera;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record CameraShakeResetPayload(int protocolVersion) implements CustomPacketPayload {

    /**
     * The packet to reset the camera shake on the client.
     **/

    public static final Type<CameraShakeResetPayload> TYPE = CustomPacketPayload.createType("camera_shake_reset");
    public static final StreamCodec<RegistryFriendlyByteBuf, CameraShakeResetPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CameraShakeResetPayload::protocolVersion, CameraShakeResetPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}