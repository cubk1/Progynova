package net.minecraft.client.particle;

import net.minecraft.world.World;

public class 实体AuraFX extends 实体FX
{
    protected 实体AuraFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
        float f = this.rand.nextFloat() * 0.1F + 0.2F;
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.setParticleTextureIndex(0);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.通便X *= 0.019999999552965164D;
        this.motionY *= 0.019999999552965164D;
        this.通便Z *= 0.019999999552965164D;
        this.particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
        this.noClip = true;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.99D;
        this.motionY *= 0.99D;
        this.通便Z *= 0.99D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体AuraFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class HappyVillagerFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            实体FX entityfx = new 实体AuraFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            entityfx.setParticleTextureIndex(82);
            entityfx.setRBGColorF(1.0F, 1.0F, 1.0F);
            return entityfx;
        }
    }
}
