package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class 实体SuspendFX extends 实体FX
{
    protected 实体SuspendFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn - 0.125D, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.particleRed = 0.4F;
        this.particleGreen = 0.4F;
        this.particleBlue = 0.7F;
        this.setParticleTextureIndex(0);
        this.setSize(0.01F, 0.01F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.通便X = xSpeedIn * 0.0D;
        this.motionY = ySpeedIn * 0.0D;
        this.通便Z = zSpeedIn * 0.0D;
        this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);

        if (this.worldObj.getBlockState(new 阻止位置(this)).getBlock().getMaterial() != Material.water)
        {
            this.setDead();
        }

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体SuspendFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
