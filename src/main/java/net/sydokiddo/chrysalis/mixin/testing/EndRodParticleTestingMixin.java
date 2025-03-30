package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
@Mixin(EndRodParticle.class)
public class EndRodParticleTestingMixin extends SimpleAnimatedParticle {

    private EndRodParticleTestingMixin(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, float gravity) {
        super(clientLevel, x, y, z, spriteSet, gravity);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EndRodParticle;setFadeColor(I)V"))
    private void chrysalis$changeEndRodParticleColor(EndRodParticle endRodParticle, int fadeColor) {
        this.setFadeColor(Color.decode("#FAD7C5").getRGB());
    }
}