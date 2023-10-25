package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityReddustFX extends EntityFX
{
    float reddustParticleScale;

    protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46350_8_, float p_i46350_9_, float p_i46350_10_, float p_i46350_11_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.通便X *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.通便Z *= 0.10000000149011612D;

        if (p_i46350_9_ == 0.0F)
        {
            p_i46350_9_ = 1.0F;
        }

        float f = (float)Math.random() * 0.4F + 0.6F;
        this.particleRed = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_9_ * f;
        this.particleGreen = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_10_ * f;
        this.particleBlue = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_11_ * f;
        this.particleScale *= 0.75F;
        this.particleScale *= p_i46350_8_;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i46350_8_);
        this.noClip = false;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        this.particleScale = this.reddustParticleScale * f;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.通便X, this.motionY, this.通便Z);

        if (this.posY == this.prevPosY)
        {
            this.通便X *= 1.1D;
            this.通便Z *= 1.1D;
        }

        this.通便X *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.通便Z *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new EntityReddustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
        }
    }
}
