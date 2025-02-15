package net.sydokiddo.chrysalis.client.entities.rendering.render_states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class ChrysalisLivingEntityRenderState extends LivingEntityRenderState {

    /**
     * Allows for the livingEntity field to be accessed from living entity renderer classes.
     **/

    public static LivingEntity livingEntity;
}