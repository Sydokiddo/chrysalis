package net.sydokiddo.chrysalis.util.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface JukeboxInteractionMob {

    /**
     * An interface that can be integrated into any mob class to allow for it to react to jukebox game events.
     **/

    default void setJukeboxPlaying(Mob mob, BlockPos blockPos, boolean jukeboxPlaying) {}

    @SuppressWarnings("all")
    public class JukeboxListener implements GameEventListener, JukeboxInteractionMob {

        public final Mob mob;
        public final PositionSource listenerSource;
        public final int listenerRadius;

        public JukeboxListener(Mob mob, final PositionSource listenerSource, final int listenerRadius) {
            this.mob = mob;
            this.listenerSource = listenerSource;
            this.listenerRadius = listenerRadius;
        }

        @Override
        public @NotNull PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRadius;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel, Holder<GameEvent> holder, GameEvent.Context context, Vec3 vec3) {
            if (holder == GameEvent.JUKEBOX_PLAY) {
                this.setJukeboxPlaying(this.mob, BlockPos.containing(vec3), true);
                return true;
            } else if (holder == GameEvent.JUKEBOX_STOP_PLAY) {
                this.setJukeboxPlaying(this.mob, BlockPos.containing(vec3), false);
                return true;
            } else {
                return false;
            }
        }
    }
}