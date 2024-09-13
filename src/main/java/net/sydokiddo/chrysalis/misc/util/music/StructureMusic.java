package net.sydokiddo.chrysalis.misc.util.music;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StructureMusic {

    public static Map<Player, ResourceLocation> playerStructures = new HashMap<>();
    public static int ticks = 0;

    private static void setCurrentStructure(Player player, Level level, String structureLocation) {

        @Nullable ResourceLocation oldStructure = playerStructures.get(player);
        ResourceLocation newStructure = Chrysalis.id("none");

        if (!Objects.equals(structureLocation, null)) newStructure = ResourceLocation.parse(structureLocation);
        if (Objects.equals(oldStructure, newStructure)) return;

        playerStructures.put(player, newStructure);

        if (level.isClientSide()) {
            ChrysalisClient.setStructureMusic(structureLocation);
            return;
        }

        ServerPlayNetworking.send((ServerPlayer) player, new StructureChangedPayload(newStructure));
    }

    public static void checkAllStructures(ServerLevel serverLevel, Player player) {

        boolean structureFound = false;

        for (Holder<Structure> structure : serverLevel.structureManager().registryAccess().registryOrThrow(Registries.STRUCTURE).asHolderIdMap()) {
            if (serverLevel.structureManager().getStructureWithPieceAt(BlockPos.containing(player.position()), structure.value()).isValid()) {
                setCurrentStructure(player, serverLevel, structure.getRegisteredName());
                structureFound = true;
                break;
            }
        }

        if (!structureFound) setCurrentStructure(player, serverLevel, null);
    }
}