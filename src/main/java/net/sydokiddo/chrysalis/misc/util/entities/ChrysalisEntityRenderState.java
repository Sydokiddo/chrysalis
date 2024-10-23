package net.sydokiddo.chrysalis.misc.util.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class ChrysalisEntityRenderState extends LivingEntityRenderState {

    public static LivingEntity livingEntity;
}