package net.sydokiddo.chrysalis.util.entities.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public record EntityDetectionRangeData(HolderSet<Item> items, String equipmentSlot, HolderSet<EntityType<?>> entities, double detectionPercentage, boolean forTesting) {

    public static final Codec<EntityDetectionRangeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(EntityDetectionRangeData::items),
        Codec.STRING.optionalFieldOf("equipment_slot", "head").forGetter(EntityDetectionRangeData::equipmentSlot),
        RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entities").forGetter(EntityDetectionRangeData::entities),
        Codec.doubleRange(0.0D, 1.0D).optionalFieldOf("detection_percentage", 1.0D).forGetter(EntityDetectionRangeData::detectionPercentage),
        Codec.BOOL.optionalFieldOf("for_testing", false).forGetter(EntityDetectionRangeData::forTesting)
    ).apply(instance, EntityDetectionRangeData::new));
}