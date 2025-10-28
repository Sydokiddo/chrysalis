package net.junebug.chrysalis.client.entities.rendering.render_states;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChrysalisLivingEntityRenderState extends LivingEntityRenderState {

    /**
     * Allows for the livingEntity field to be accessed from living entity renderer classes.
     **/

    public static LivingEntity livingEntity;
}