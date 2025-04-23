package net.sydokiddo.chrysalis.util.entities.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;

public record PlayerLootTableData(String uuid, ResourceKey<LootTable> lootTable, boolean forTesting) {

    public static final Codec<PlayerLootTableData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("uuid").forGetter(PlayerLootTableData::uuid),
        ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(PlayerLootTableData::lootTable),
        Codec.BOOL.optionalFieldOf(ComponentHelper.forTestingString, false).forGetter(PlayerLootTableData::forTesting)
    ).apply(instance, PlayerLootTableData::new));
}