package net.sydokiddo.chrysalis.client.entities.rendering.render_states;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChrysalisEntityRenderState extends EntityRenderState {

    /**
     * Allows for the entity field to be accessed from entity renderer classes.
     **/

    public static Entity entity;
}