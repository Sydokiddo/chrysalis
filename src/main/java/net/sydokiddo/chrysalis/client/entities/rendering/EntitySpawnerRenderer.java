package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.entities.rendering.render_states.EntitySpawnerRenderState;
import net.sydokiddo.chrysalis.common.entities.custom_entities.EntitySpawner;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EntitySpawnerRenderer extends EntityRenderer<EntitySpawner, EntitySpawnerRenderState> {

    private final EntityRenderDispatcher entityRenderer;

    public EntitySpawnerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.entityRenderer = context.getEntityRenderDispatcher();
    }

    @Override
    public @NotNull EntitySpawnerRenderState createRenderState() {
        return new EntitySpawnerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EntitySpawner entitySpawner, @NotNull EntitySpawnerRenderState renderState, float tickRate) {
        EntitySpawnerRenderState.entityToSpawn = entitySpawner.entityToSpawn;
        super.extractRenderState(entitySpawner, renderState, tickRate);
    }

    @Override
    public void render(@NotNull EntitySpawnerRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();
        float scale;

        if (renderState.ageInTicks <= 50.0F) {
            scale = Math.min(renderState.ageInTicks, 50.0F) / 50.0F;
            poseStack.scale(scale, scale, scale);
        }

        scale = Mth.wrapDegrees(renderState.ageInTicks * 40.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(scale));

        Entity entity = EntitySpawner.createEntity(EntitySpawnerRenderState.entityToSpawn, EntitySpawnerRenderState.entity.level());
        assert entity != null;

        entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, renderState.partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}