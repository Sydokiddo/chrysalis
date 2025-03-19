package net.sydokiddo.chrysalis.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {

    /**
     * Puts a message in the server console with the specific Chrysalis version the user has upon joining a world.
     **/

    @Inject(method = "placeNewPlayer", at = @At(value = "TAIL"))
    private void chrysalis$onPlayerConnectToServer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo info) {
        DebugHelper.sendLoggedInMessage(Chrysalis.LOGGER, serverPlayer, Chrysalis.LOGGER.getName(), Chrysalis.CHRYSALIS_VERSION);
    }

    @Unique private Inventory chrysalis$savedInventory;

    @Inject(method = "respawn", at = @At("HEAD"))
    private void chrysalis$saveInventory(ServerPlayer serverPlayer, boolean keepInventory, Entity.RemovalReason reason, CallbackInfoReturnable<ServerPlayer> cir) {
        this.chrysalis$savedInventory = serverPlayer.getInventory();
    }

    @ModifyVariable(method = "respawn", at = @At("TAIL"), ordinal = 1)
    private ServerPlayer chrysalis$setInventory(ServerPlayer serverPlayer) {
        serverPlayer.getInventory().replaceWith(this.chrysalis$savedInventory);
        return serverPlayer;
    }
}