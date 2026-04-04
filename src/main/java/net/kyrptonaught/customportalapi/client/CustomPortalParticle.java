package net.kyrptonaught.customportalapi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.ColorUtil;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import org.jspecify.annotations.NonNull;

public class CustomPortalParticle extends PortalParticle {
    protected CustomPortalParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, TextureAtlasSprite sprite) {
        super(clientWorld, d, e, f, g, h, i, sprite);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        
        public Particle createParticle(BlockParticleOption blockStateParticleEffect, @NonNull ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, @NonNull RandomSource random) {    // Particle portalParticle = (new PortalParticle.Factory(spriteProvider).createParticle(null,clientWorld,d,e,f,g,h,i));
            CustomPortalParticle portalParticle = new CustomPortalParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider.get(random));
            Block block = blockStateParticleEffect.getState().getBlock();
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
            if (link != null) {
                float[] rgb = ColorUtil.getColorForBlock(link.colorID);
                portalParticle.setColor(rgb[0], rgb[1], rgb[2]);
            }
            return portalParticle;
        }
    }
}
