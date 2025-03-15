package net.sydokiddo.chrysalis.util.entities.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public record ChargedMobDropData(HolderSet<EntityType<?>> entities, Holder<Item> droppedItem) {

    public static final Codec<ChargedMobDropData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entities").forGetter(ChargedMobDropData::entities),
        Item.CODEC.fieldOf("dropped_item").forGetter(ChargedMobDropData::droppedItem)
    ).apply(instance, ChargedMobDropData::new));
}