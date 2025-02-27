package com.teamabnormals.neapolitan.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlyParticle extends TextureSheetParticle {
	protected final SpriteSet animatedSprite;
	private final float speed;
	private float angle;
	private final float turnSpeed;

	private FlyParticle(SpriteSet animatedSpriteIn, ClientLevel world, double x, double y, double z, float speedIn, float angleIn, float turnSpeedIn) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.xd *= 0.01;
		this.yd *= 0.01;
		this.zd *= 0.01;
		this.speed = speedIn;
		this.angle = angleIn;
		this.turnSpeed = turnSpeedIn;
		this.gravity = 1.0F;
		this.lifetime = 40;
		this.animatedSprite = animatedSpriteIn;
		this.setSpriteFromAge(this.animatedSprite);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			Vec3 vector3d = Vec3.directionFromRotation(0.0F, this.angle);
			this.xd = vector3d.x * (double) this.speed;
			this.yd = Math.sin(this.age * 0.3F) * 0.08D + 0.006D;
			this.zd = vector3d.z * (double) this.speed;
			this.move(this.xd, this.yd, this.zd);
			this.angle += this.turnSpeed;
			while (this.angle > 180.0F) {
				this.angle -= 360.0F;
			}
			while (this.angle < -180.0F) {
				this.angle += 360.0F;
			}
			this.setSpriteFromAge(this.animatedSprite);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet animatedSprite;

		public Provider(SpriteSet animatedSpriteIn) {
			this.animatedSprite = animatedSpriteIn;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new FlyParticle(this.animatedSprite, worldIn, x, y, z, (float) xSpeed, (float) ySpeed, (float) zSpeed);
		}
	}
}