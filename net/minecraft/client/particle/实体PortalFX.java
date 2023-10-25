package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.world.World;

public class 实体PortalFX extends 实体FX
{
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;

    protected 实体PortalFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.通便X = xSpeedIn;
        this.motionY = ySpeedIn;
        this.通便Z = zSpeedIn;
        this.portalPosX = this.X坐标 = xCoordIn;
        this.portalPosY = this.Y坐标 = yCoordIn;
        this.portalPosZ = this.Z坐标 = zCoordIn;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleGreen *= 0.3F;
        this.particleRed *= 0.9F;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 8.0D));
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        f = 1.0F - f;
        f = f * f;
        f = 1.0F - f;
        this.particleScale = this.portalParticleScale * f;
        super.renderParticle(worldRendererIn, 实体In, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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
        f1 = f1 * f1 * f1 * f1;
        return f * (1.0F - f1) + f1;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        this.X坐标 = this.portalPosX + this.通便X * (double)f;
        this.Y坐标 = this.portalPosY + this.motionY * (double)f + (double)(1.0F - f);
        this.Z坐标 = this.portalPosZ + this.通便Z * (double)f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体PortalFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
