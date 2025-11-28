package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.entities.models.KeyGolemModel;
import net.junebug.chrysalis.client.entities.rendering.render_states.CLivingEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class KeyGolemRenderer extends MobRenderer<KeyGolem, CLivingEntityRenderState, KeyGolemModel> {

    /**
     * The entity rendering class for key golems.
     **/

    public KeyGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new KeyGolemModel(context.bakeLayer(KeyGolemModel.LAYER)), 0.2F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CLivingEntityRenderState renderState) {
        return Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_golden.png");
    }

    @Override
    public @NotNull CLivingEntityRenderState createRenderState() {
        return new CLivingEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull KeyGolem keyGolem, @NotNull CLivingEntityRenderState renderState, float tickCount) {
        CLivingEntityRenderState.livingEntity = keyGolem;
        super.extractRenderState(keyGolem, renderState, tickCount);
    }

    @Override
    protected int getBlockLightLevel(@NotNull KeyGolem keyGolem, @NotNull BlockPos blockPos) {
        return 15;
    }
}