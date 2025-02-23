package net.sydokiddo.chrysalis.util.blocks.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public record BlockPropertyTransformer(HolderSet<Block> blocks, float destroyTime, boolean requiresTool, int lightLevel, boolean emissiveRendering, boolean replaceable, boolean ignitedByLava, boolean spawnsTerrainParticles) {

    /**
     * Converts information from a json file into a specified block(s)'s properties.
     **/

    public static final Codec<BlockPropertyTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockPropertyTransformer::blocks),
        Codec.floatRange(-1.0F, Float.MAX_VALUE).optionalFieldOf("destroy_time", 1.5F).forGetter(BlockPropertyTransformer::destroyTime),
        Codec.BOOL.optionalFieldOf("requires_tool", false).forGetter(BlockPropertyTransformer::requiresTool),
        Codec.intRange(0, 15).optionalFieldOf("light_level", 0).forGetter(BlockPropertyTransformer::lightLevel),
        Codec.BOOL.optionalFieldOf("emissive_rendering", false).forGetter(BlockPropertyTransformer::emissiveRendering),
        Codec.BOOL.optionalFieldOf("replaceable", false).forGetter(BlockPropertyTransformer::replaceable),
        Codec.BOOL.optionalFieldOf("ignited_by_lava", false).forGetter(BlockPropertyTransformer::ignitedByLava),
        Codec.BOOL.optionalFieldOf("spawns_terrain_particles", true).forGetter(BlockPropertyTransformer::spawnsTerrainParticles)
    ).apply(instance, BlockPropertyTransformer::new));
}