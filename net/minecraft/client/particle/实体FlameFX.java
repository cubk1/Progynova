package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体FlameFX extends 实体FX
{
    private float flameScale;

    protected 实体FlameFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.通便X = this.通便X * 0.009999999776482582D + xSpeedIn;
        this.motionY = this.motionY * 0.009999999776482582D + ySpeedIn;
        this.通便Z = this.通便Z * 0.009999999776482582D + zSpeedIn;
        this.X坐标 += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.Y坐标 += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.Z坐标 += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.flameScale = this.particleScale;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.noClip = true;
        this.setParticleTextureIndex(48);
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
        super.renderParticle(worldRendererIn, 实体In, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public int getBrightnessForRender(float partialTicks)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTicks);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    public float getBrightness(float partialTicks)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        float f1 = super.getBrightness(partialTicks);
        return f1 * f + (1.0F - f);
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
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体FlameFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
