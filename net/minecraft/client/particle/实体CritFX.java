package net.minecraft.client.particle;

import net.minecraft.world.World;

public class 实体CritFX extends 实体SmokeFX
{
    protected 实体CritFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1201_8_, double p_i1201_10_, double p_i1201_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1201_8_, p_i1201_10_, p_i1201_12_, 2.5F);
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体CritFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
