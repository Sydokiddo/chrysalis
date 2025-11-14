package net.junebug.chrysalis.util.entities.interfaces;

public interface ChargedMob {

    /**
     * An interface for charged mobs that can cause other mobs to drop heads upon exploding.
     **/

    default boolean canDropMobsSkull() {
        return false;
    }

    default void onChargedExplode() {}
}