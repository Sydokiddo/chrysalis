package net.sydokiddo.chrysalis.registry;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.client.particles.options.LargePulsationParticleOptions;
import net.sydokiddo.chrysalis.client.particles.options.SmallPulsationParticleOptions;
import java.awt.*;

public class ChrysalisTesting {

    public static void emitPulsationParticle(Level level, Entity entity, Direction direction, int type, boolean muffled) {

        if (entity.isCrouching() || !(level instanceof ServerLevel serverLevel)) return;

        int color = Color.decode("#8C62DB").getRGB();
        ParticleOptions particleOptions = null;
        ParticleOptions smallPulsation = new SmallPulsationParticleOptions(color, false, direction.get3DDataValue(), 10);
        ParticleOptions largePulsation = new LargePulsationParticleOptions(color, false, direction.get3DDataValue(), 10);

        switch (type) {

            case 1 -> particleOptions = entity.getDeltaMovement().horizontalDistance() >= 2.0D ? largePulsation : smallPulsation; // Projectiles
            case 2 -> particleOptions = entity.fallDistance >= 6.0F ? largePulsation : smallPulsation; // Falling

            default -> { // Walking
                if (entity.getDeltaMovement().y() <= -0.07D) particleOptions = smallPulsation;
            }
        }

        Vec3 pulsationPos = new Vec3(entity.getX(), entity.getY() - (direction == Direction.UP && entity.getBlockStateOn().is(BlockTags.WALLS) ? 0.5F : 0.0F), entity.getZ());
        float volume = muffled ? 0.25F : 0.5F;

        if (particleOptions != null) {
            if (particleOptions == largePulsation) volume = volume * 2.0F;
            serverLevel.sendParticles(particleOptions, pulsationPos.x(), pulsationPos.y(), pulsationPos.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            level.playSound(null, pulsationPos.x(), pulsationPos.y(), pulsationPos.z(), SoundEvents.NOTE_BLOCK_HARP, SoundSource.BLOCKS, volume, 0.5F + level.getRandom().nextFloat() * 1.2F);
        }
    }
}