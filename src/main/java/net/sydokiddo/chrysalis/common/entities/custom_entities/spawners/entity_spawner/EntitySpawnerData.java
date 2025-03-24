package net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.entity_spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import java.awt.*;
import java.util.List;

public class EntitySpawnerData {

    /**
     * The base codec for data driven entity spawner properties.
     **/

    private record SoundsData(Holder<SoundEvent> appear, Holder<SoundEvent> aboutToSpawnEntity, Holder<SoundEvent> spawnEntity) {

        public static final Codec<SoundsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.optionalFieldOf("appear", ChrysalisSoundEvents.ENTITY_SPAWNER_APPEAR).forGetter(SoundsData::appear),
            SoundEvent.CODEC.optionalFieldOf("about_to_spawn_entity", ChrysalisSoundEvents.ENTITY_SPAWNER_ABOUT_TO_SPAWN_ENTITY).forGetter(SoundsData::aboutToSpawnEntity),
            SoundEvent.CODEC.optionalFieldOf("spawn_entity", ChrysalisSoundEvents.ENTITY_SPAWNER_SPAWN_ENTITY).forGetter(SoundsData::aboutToSpawnEntity)
        ).apply(instance, SoundsData::new));
    }

    private record ParticlesData(String startingColor, String endingColor, ParticleOptions spawnParticle) {

        public static final Codec<ParticlesData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("starting_color", "#FF6A00").forGetter(ParticlesData::startingColor),
            Codec.STRING.optionalFieldOf("ending_color", "#FFFFFF").forGetter(ParticlesData::endingColor),
            ParticleTypes.CODEC.optionalFieldOf("spawn_particle", ParticleTypes.FLAME).forGetter(ParticlesData::spawnParticle)
        ).apply(instance, ParticlesData::new));
    }

    private record DelayData(int min, int max) {

        public static final Codec<DelayData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("min", 60).forGetter(DelayData::min),
            Codec.INT.optionalFieldOf("max", 129).forGetter(DelayData::max)
        ).apply(instance, DelayData::new));
    }

    public record EntitySpawnerConfig(String id, List<SoundsData> sounds, List<ParticlesData> particles, SimpleWeightedRandomList<SpawnData> spawnPotentials, List<DelayData> delay) {

        public static final Codec<EntitySpawnerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(EntitySpawnerConfig::id),
            SoundsData.CODEC.listOf().fieldOf("sounds").forGetter(EntitySpawnerConfig::sounds),
            ParticlesData.CODEC.listOf().fieldOf("particles").forGetter(EntitySpawnerConfig::particles),
            SimpleWeightedRandomList.wrappedCodec(SpawnData.CODEC).fieldOf("spawn_potentials").forGetter(EntitySpawnerConfig::spawnPotentials),
            DelayData.CODEC.listOf().fieldOf("delay").forGetter(EntitySpawnerConfig::delay)
        ).apply(instance, EntitySpawnerConfig::new));

        public String getId() {
            return this.id();
        }

        public Holder<SoundEvent> getAppearSound() {
            return this.sounds().getFirst().appear();
        }

        public Holder<SoundEvent> getAboutToSpawnEntitySound() {
            return this.sounds().getFirst().aboutToSpawnEntity();
        }

        public Holder<SoundEvent> getSpawnEntitySound() {
            return this.sounds().getFirst().spawnEntity();
        }

        public int getStartingColorFromString() {
            return Color.decode(this.particles().getFirst().startingColor()).getRGB();
        }

        public int getEndingColorFromString() {
            return Color.decode(this.particles().getFirst().endingColor()).getRGB();
        }

        public ParticleOptions getSpawnParticle() {
            return this.particles().getFirst().spawnParticle();
        }

        public int getMinDelay() {
            return this.delay().getFirst().min();
        }

        public int getMaxDelay() {
            return this.delay().getFirst().max();
        }
    }
}