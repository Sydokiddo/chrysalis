package net.sydokiddo.chrysalis.util.entities.interfaces;

public interface ChargedMob {

    default boolean canDropMobsSkull() {
        return false;
    }

    default void onChargedExplode() {}
}