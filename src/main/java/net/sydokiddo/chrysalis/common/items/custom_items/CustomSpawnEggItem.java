package net.sydokiddo.chrysalis.common.items.custom_items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class CustomSpawnEggItem extends SpawnEggItem {

    private final EntityType<? extends Mob> mobOffspring;

    public CustomSpawnEggItem(EntityType<? extends Mob> entityType, EntityType<? extends Mob> mobOffspring, Properties properties) {
        super(entityType, properties);
        this.mobOffspring = mobOffspring;
    }

    /**
     * Gets the custom offspring for the adult mob and spawns it when the adult mob is right-clicked on with the spawn egg.
     **/

    @Override
    public @NotNull Optional<Mob> spawnOffspringFromSpawnEgg(@NotNull Player player, @NotNull Mob mob, @NotNull EntityType<? extends Mob> entityType, @NotNull ServerLevel serverLevel, @NotNull Vec3 vec3, @NotNull ItemStack itemStack) {

        if (this.mobOffspring == null) return super.spawnOffspringFromSpawnEgg(player, mob, entityType, serverLevel, vec3, itemStack);
        Mob babyMob = this.mobOffspring.create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
        if (babyMob == null || entityType == this.mobOffspring) return Optional.empty();

        babyMob.setBaby(true);
        babyMob.moveTo(vec3.x(), vec3.y(), vec3.z(), 0.0F, 0.0F);
        serverLevel.addFreshEntityWithPassengers(babyMob);

        if (itemStack.has(DataComponents.CUSTOM_NAME)) babyMob.setCustomName(itemStack.getHoverName());
        itemStack.consume(1, player);

        return Optional.of(babyMob);
    }
}