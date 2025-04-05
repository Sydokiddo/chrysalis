package net.sydokiddo.chrysalis.util.blocks.dispensers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class PlaceEntityDispenserBehavior implements DispenseItemBehavior {

    /**
     * Allows for dispensers to place any custom entity.
     **/

    private final EntityType<?> entityType;

    private final Position
        requiredRange,
        placementOffset
    ;

    private final TagKey<Block> requiredBaseBlock;
    private final SoundEvent placeSound;

    public PlaceEntityDispenserBehavior(EntityType<?> entityType, Position requiredRange, Position placementOffset, TagKey<Block> requiredBaseBlock, SoundEvent placeSound) {
        this.entityType = entityType;
        this.requiredRange = requiredRange;
        this.placementOffset = placementOffset;
        this.requiredBaseBlock = requiredBaseBlock;
        this.placeSound = placeSound;
    }

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, @NotNull ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        BlockPos blockPos = blockSource.pos().relative(direction);
        ServerLevel serverLevel = blockSource.level();

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();
        List<Entity> entitiesOnBlock = serverLevel.getEntities(null, new AABB(x, y, z, x + this.requiredRange.x(), y + this.requiredRange.y(), z + this.requiredRange.z()));

        if (BlockHelper.isFree(serverLevel.getBlockState(blockPos)) && entitiesOnBlock.isEmpty()) {

            if (this.requiredBaseBlock != null && !serverLevel.getBlockState(blockPos.below()).is(this.requiredBaseBlock)) return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, direction);

            if (this.entityType == null) return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
            Entity entity = this.entityType.create(serverLevel, EntitySpawnReason.DISPENSER);
            assert entity != null;
            entity.setPos(x + this.placementOffset.x(), y + this.placementOffset.y(), z + this.placementOffset.z());
            serverLevel.addFreshEntity(entity);

            if (entity instanceof EndCrystal endCrystal) {
                endCrystal.setShowBottom(false);
                EndDragonFight endDragonFight = serverLevel.getDragonFight();
                if (endDragonFight != null) endDragonFight.tryRespawn();
            }

            serverLevel.gameEvent(null, GameEvent.ENTITY_PLACE, blockPos);
            if (this.placeSound != null) serverLevel.playSound(null, blockPos, this.placeSound, entity.getSoundSource(), 1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);

            DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, this.entityType.toString(), blockPos);
            itemStack.shrink(1);
            return itemStack;
        }

        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}