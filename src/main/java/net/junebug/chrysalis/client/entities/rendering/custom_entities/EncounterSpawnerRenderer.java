package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.entities.rendering.render_states.ChrysalisEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.encounter_spawner.EncounterSpawner;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class EncounterSpawnerRenderer extends EntityRenderer<EncounterSpawner, ChrysalisEntityRenderState> {

    private final EntityRenderDispatcher entityRenderer;

    public EncounterSpawnerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.entityRenderer = context.getEntityRenderDispatcher();
    }

    @Override
    public @NotNull ChrysalisEntityRenderState createRenderState() {
        return new ChrysalisEntityRenderState();
    }

    @Override
    public void render(@NotNull ChrysalisEntityRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {

        Player player = Minecraft.getInstance().player;
        if (!(ChrysalisEntityRenderState.entity instanceof EncounterSpawner encounterSpawner) || encounterSpawner.getEntityToSpawn().isEmpty() || player != null && !player.getAbilities().invulnerable) return;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(renderState.ageInTicks * 10.0F)));

        Optional<Entity> entity = EncounterSpawner.createEntity(encounterSpawner);
        entity.ifPresent(value -> this.entityRenderer.render(value, 0.0D, 0.0D, 0.0D, renderState.partialTick, poseStack, bufferSource, packedLight));
        poseStack.popPose();
    }
}