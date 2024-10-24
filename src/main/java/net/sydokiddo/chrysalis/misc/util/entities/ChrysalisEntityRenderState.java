package net.sydokiddo.chrysalis.misc.util.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class ChrysalisEntityRenderState extends EntityRenderState {

    public static Entity entity;
    public static LivingEntity livingEntity;
}