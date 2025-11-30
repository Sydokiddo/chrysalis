package net.junebug.chrysalis.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.junebug.chrysalis.Chrysalis;
import net.minecraft.Util;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public class CRenderTypes {

    /**
     * Custom render types that can be used by various sources.
     **/

    // region Entity Cutout Emissive

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_EMISSIVE = Util.memoize((resourceLocation, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
            .setShaderState(new RenderStateShard.ShaderStateShard(register("render_type_entity_cutout_emissive", DefaultVertexFormat.NEW_ENTITY)))
            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, TriState.FALSE, false))
            .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(outline);
        return RenderType.create("entity_cutout_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, false, compositeState);
    });

    public static RenderType entityCutoutEmissive(ResourceLocation resourceLocation, boolean outline) {
        return ENTITY_CUTOUT_EMISSIVE.apply(resourceLocation, outline);
    }

    public static RenderType entityCutoutEmissive(ResourceLocation resourceLocation) {
        return entityCutoutEmissive(resourceLocation, true);
    }

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static ShaderProgram register(String name, VertexFormat vertexFormat) {
        return register(name, vertexFormat, ShaderDefines.EMPTY);
    }

    @SuppressWarnings("all")
    private static ShaderProgram register(String name, VertexFormat vertexFormat, ShaderDefines shaderDefines) {
        ShaderProgram shaderProgram = new ShaderProgram(Chrysalis.resourceLocationId("core/" + name), vertexFormat, shaderDefines);
        CoreShaders.getProgramsToPreload().add(shaderProgram);
        return shaderProgram;
    }

    // endregion
}