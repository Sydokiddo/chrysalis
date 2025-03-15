package net.sydokiddo.chrysalis.util.blocks.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public record BlockPropertyData(HolderSet<Block> blocks, float destroyTime, boolean requiresTool, int lightLevel, boolean emissiveRendering, boolean replaceable, boolean ignitedByLava, boolean spawnsTerrainParticles, boolean forTesting) {

    /**
     * Converts information from a json file into a specified block(s)'s properties.
     **/

    public static final Codec<BlockPropertyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockPropertyData::blocks),
        Codec.floatRange(-1.0F, Float.MAX_VALUE).fieldOf("destroy_time").forGetter(BlockPropertyData::destroyTime),
        Codec.BOOL.fieldOf("requires_tool").forGetter(BlockPropertyData::requiresTool),
        Codec.intRange(0, 15).fieldOf("light_level").forGetter(BlockPropertyData::lightLevel),
        Codec.BOOL.fieldOf("emissive_rendering").forGetter(BlockPropertyData::emissiveRendering),
        Codec.BOOL.fieldOf("replaceable").forGetter(BlockPropertyData::replaceable),
        Codec.BOOL.fieldOf("ignited_by_lava").forGetter(BlockPropertyData::ignitedByLava),
        Codec.BOOL.fieldOf("spawns_terrain_particles").forGetter(BlockPropertyData::spawnsTerrainParticles),
        Codec.BOOL.optionalFieldOf("for_testing", false).forGetter(BlockPropertyData::forTesting)
    ).apply(instance, BlockPropertyData::new));
}