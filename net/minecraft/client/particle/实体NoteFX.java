package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体NoteFX extends 实体FX
{
    float noteParticleScale;

    protected 实体NoteFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46353_8_, double p_i46353_10_, double p_i46353_12_)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0F);
    }

    protected 实体NoteFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1217_8_, double p_i1217_10_, double p_i1217_12_, float p_i1217_14_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.通便X *= 0.009999999776482582D;
        this.motionY *= 0.009999999776482582D;
        this.通便Z *= 0.009999999776482582D;
        this.motionY += 0.2D;
        this.particleRed = MathHelper.sin(((float)p_i1217_8_ + 0.0F) * (float)Math.PI * 2.0F) * 0.65F + 0.35F;
        this.particleGreen = MathHelper.sin(((float)p_i1217_8_ + 0.33333334F) * (float)Math.PI * 2.0F) * 0.65F + 0.35F;
        this.particleBlue = MathHelper.sin(((float)p_i1217_8_ + 0.6666667F) * (float)Math.PI * 2.0F) * 0.65F + 0.35F;
        this.particleScale *= 0.75F;
        this.particleScale *= p_i1217_14_;
        this.noteParticleScale = this.particleScale;
        this.particleMaxAge = 6;
        this.noClip = false;
        this.setParticleTextureIndex(64);
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        this.particleScale = this.noteParticleScale * f;
        super.renderParticle(worldRendererIn, 实体In, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.moveEntity(this.通便X, this.motionY, this.通便Z);

        if (this.Y坐标 == this.prevPosY)
        {
            this.通便X *= 1.1D;
            this.通便Z *= 1.1D;
        }

        this.通便X *= 0.6600000262260437D;
        this.motionY *= 0.6600000262260437D;
        this.通便Z *= 0.6600000262260437D;

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体NoteFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
