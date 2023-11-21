package net.sydokiddo.chrysalis.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.sydokiddo.chrysalis.Chrysalis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {

    /**
     * Puts a message in the server console with the specific Chrysalis version the user has upon joining a world.
     **/

    @Inject(method = "placeNewPlayer", at = @At(value = "TAIL"))
    private void chrysalis$onPlayerConnectToServer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        Chrysalis.LOGGER.info(serverPlayer.getName().getString() + " has " + Chrysalis.LOGGER.getName() + " v" + Chrysalis.CHRYSALIS_VERSION + " installed");
    }
}