package net.minecraft.client.particle;

import net.minecraft.world.World;

public class 实体EnchantmentTableParticleFX extends 实体FX
{
    private float field_70565_a;
    private double coordX;
    private double coordY;
    private double coordZ;

    protected 实体EnchantmentTableParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.通便X = xSpeedIn;
        this.motionY = ySpeedIn;
        this.通便Z = zSpeedIn;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        this.X坐标 = this.prevPosX = xCoordIn + xSpeedIn;
        this.Y坐标 = this.prevPosY = yCoordIn + ySpeedIn;
        this.Z坐标 = this.prevPosZ = zCoordIn + zSpeedIn;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.field_70565_a = this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleGreen *= 0.9F;
        this.particleRed *= 0.9F;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
    }

    public int getBrightnessForRender(float partialTicks)
    {
        int i = super.getBrightnessForRender(partialTicks);
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int)(f * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

    public float getBrightness(float partialTicks)
    {
        float f = super.getBrightness(partialTicks);
        float f1 = (float)this.particleAge / (float)this.particleMaxAge;
        f1 = f1 * f1;
        f1 = f1 * f1;
        return f * (1.0F - f1) + f1;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 = f1 * f1;
        f1 = f1 * f1;
        this.X坐标 = this.coordX + this.通便X * (double)f;
        this.Y坐标 = this.coordY + this.motionY * (double)f - (double)(f1 * 1.2F);
        this.Z坐标 = this.coordZ + this.通便Z * (double)f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }
    }

    public static class EnchantmentTable implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体EnchantmentTableParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
