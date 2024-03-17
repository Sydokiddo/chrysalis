package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class CSpawnEggItem extends SpawnEggItem {

    private final EntityType<? extends Mob> mobOffspring;

    public CSpawnEggItem(EntityType<? extends Mob> entityType, int baseColor, int spotsColor, EntityType<? extends Mob> mobOffspring, Properties properties) {
        super(entityType, baseColor, spotsColor, properties);
        this.mobOffspring = mobOffspring;
    }

    /**
     * Gets the custom offspring for the adult mob and spawns it when the adult mob is right-clicked with the spawn egg.
     **/

    @Override
    public @NotNull Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob mob, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 vec3, ItemStack itemStack) {

        if (this.mobOffspring == null) return super.spawnOffspringFromSpawnEgg(player, mob, entityType, serverLevel, vec3, itemStack);

        Mob babyMob = this.mobOffspring.create(serverLevel);

        if (babyMob == null || entityType == this.mobOffspring) return Optional.empty();

        babyMob.setBaby(true);
        babyMob.moveTo(vec3.x(), vec3.y(), vec3.z(), 0.0F, 0.0F);
        serverLevel.addFreshEntityWithPassengers(babyMob);

        if (itemStack.hasCustomHoverName()) babyMob.setCustomName(itemStack.getHoverName());
        if (!player.getAbilities().instabuild) itemStack.shrink(1);

        return Optional.of(babyMob);
    }
}