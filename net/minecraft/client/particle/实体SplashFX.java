package net.minecraft.client.particle;

import net.minecraft.world.World;

public class 实体SplashFX extends 实体RainFX
{
    protected 实体SplashFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleGravity = 0.04F;
        this.nextTextureIndexX();

        if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D))
        {
            this.通便X = xSpeedIn;
            this.motionY = ySpeedIn + 0.1D;
            this.通便Z = zSpeedIn;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体SplashFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
