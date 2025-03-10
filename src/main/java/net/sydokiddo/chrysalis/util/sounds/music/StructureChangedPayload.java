package net.sydokiddo.chrysalis.util.sounds.music;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sydokiddo.chrysalis.registry.ChrysalisClientRegistry;
import org.jetbrains.annotations.NotNull;

public record StructureChangedPayload(ResourceLocation structureName) implements CustomPacketPayload {

    /**
     * The packet to update the player's current structure on the client.
     **/

    public static final Type<StructureChangedPayload> TYPE = CustomPacketPayload.createType("structure_changed");
    public static final StreamCodec<FriendlyByteBuf, StructureChangedPayload> CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, StructureChangedPayload::structureName, StructureChangedPayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public static void handleDataOnClient(final StructureChangedPayload payload, final IPayloadContext context) {
        ChrysalisClientRegistry.setStructureMusic(payload.structureName().toString(), true);
    }
}