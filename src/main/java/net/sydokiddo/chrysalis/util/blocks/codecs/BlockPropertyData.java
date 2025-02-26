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
        Codec.floatRange(-1.0F, Float.MAX_VALUE).optionalFieldOf("destroy_time", 1.5F).forGetter(BlockPropertyData::destroyTime),
        Codec.BOOL.optionalFieldOf("requires_tool", false).forGetter(BlockPropertyData::requiresTool),
        Codec.intRange(0, 15).optionalFieldOf("light_level", 0).forGetter(BlockPropertyData::lightLevel),
        Codec.BOOL.optionalFieldOf("emissive_rendering", false).forGetter(BlockPropertyData::emissiveRendering),
        Codec.BOOL.optionalFieldOf("replaceable", false).forGetter(BlockPropertyData::replaceable),
        Codec.BOOL.optionalFieldOf("ignited_by_lava", false).forGetter(BlockPropertyData::ignitedByLava),
        Codec.BOOL.optionalFieldOf("spawns_terrain_particles", true).forGetter(BlockPropertyData::spawnsTerrainParticles),
        Codec.BOOL.optionalFieldOf("for_testing", false).forGetter(BlockPropertyData::forTesting)
    ).apply(instance, BlockPropertyData::new));
}