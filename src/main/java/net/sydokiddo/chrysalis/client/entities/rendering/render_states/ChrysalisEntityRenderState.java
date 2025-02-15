package net.sydokiddo.chrysalis.client.entities.rendering.render_states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class ChrysalisEntityRenderState extends EntityRenderState {

    /**
     * Allows for the entity field to be accessed from entity renderer classes.
     **/

    public static Entity entity;
}