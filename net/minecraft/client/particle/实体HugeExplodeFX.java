package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class 实体HugeExplodeFX extends 实体FX
{
    private int timeSinceStart;
    private int maximumTime = 8;

    protected 实体HugeExplodeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    }

    public void onUpdate()
    {
        for (int i = 0; i < 6; ++i)
        {
            double d0 = this.X坐标 + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double d1 = this.Y坐标 + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double d2 = this.Z坐标 + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D, new int[0]);
        }

        ++this.timeSinceStart;

        if (this.timeSinceStart == this.maximumTime)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 1;
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体HugeExplodeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
