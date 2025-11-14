package net.junebug.chrysalis.util.entities.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.junebug.chrysalis.util.helpers.ComponentHelper;

public record PlayerLootTableData(String uuid, ResourceKey<LootTable> lootTable, String enabled) {

    /**
     * Converts information from a json file into a loot table that players can drop upon dying.
     **/

    public static final Codec<PlayerLootTableData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("uuid").forGetter(PlayerLootTableData::uuid),
        ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(PlayerLootTableData::lootTable),
        Codec.STRING.optionalFieldOf(ComponentHelper.enabledString, ComponentHelper.trueString).forGetter(PlayerLootTableData::enabled)
    ).apply(instance, PlayerLootTableData::new));
}