package net.sydokiddo.chrysalis.mixin.testing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(EndRodParticle.class)
public class EndRodParticleMixin extends SimpleAnimatedParticle {

    private EndRodParticleMixin(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, float gravity) {
        super(clientLevel, x, y, z, spriteSet, gravity);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EndRodParticle;setFadeColor(I)V"))
    private void chrysalis$changeEndRodParticleColor(EndRodParticle endRodParticle, int fadeColor) {
        this.setFadeColor(Color.decode("#FAD7C5").getRGB());
    }
}