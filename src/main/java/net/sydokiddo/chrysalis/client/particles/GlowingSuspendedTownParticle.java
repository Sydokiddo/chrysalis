package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class GlowingSuspendedTownParticle extends TextureSheetParticle {

    // Initialization and Ticking

    private GlowingSuspendedTownParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f, g, h, i);
        float j = this.random.nextFloat() * 0.1F + 0.2F;
        this.rCol = j;
        this.gCol = j;
        this.bCol = j;
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
        this.xd *= 0.019999999552965164;
        this.yd *= 0.019999999552965164;
        this.zd *= 0.019999999552965164;
        this.lifetime = (int)(20.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.99;
            this.yd *= 0.99;
            this.zd *= 0.99;
        }
    }

    // Rendering

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public int getLightColor(float f) {
        float g = ((float)this.age) / (float)this.lifetime;
        g = Mth.clamp(g, 1.0f, 0.0f);
        int i = super.getLightColor(f);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(g * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // Providers

    @Environment(EnvType.CLIENT)
    public static class GlowingParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public GlowingParticleProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            GlowingSuspendedTownParticle particle = new GlowingSuspendedTownParticle(clientLevel, d, e, f, g, h, i);
            particle.pickSprite(this.sprite);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }
}