package net.minecraft.client.particle;

import net.minecraft.world.World;

public class 实体FishWakeFX extends 实体FX
{
    protected 实体FishWakeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i45073_8_, double p_i45073_10_, double p_i45073_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.通便X *= 0.30000001192092896D;
        this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        this.通便Z *= 0.30000001192092896D;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setParticleTextureIndex(19);
        this.setSize(0.01F, 0.01F);
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleGravity = 0.0F;
        this.通便X = p_i45073_8_;
        this.motionY = p_i45073_10_;
        this.通便Z = p_i45073_12_;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.motionY -= (double)this.particleGravity;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.通便Z *= 0.9800000190734863D;
        int i = 60 - this.particleMaxAge;
        float f = (float)i * 0.001F;
        this.setSize(f, f);
        this.setParticleTextureIndex(19 + i % 4);

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体FishWakeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
