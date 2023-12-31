package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体DropParticleFX extends 实体FX
{
    private Material materialType;
    private int bobTimer;

    protected 实体DropParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.通便X = this.motionY = this.通便Z = 0.0D;

        if (p_i1203_8_ == Material.water)
        {
            this.particleRed = 0.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 1.0F;
        }
        else
        {
            this.particleRed = 1.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 0.0F;
        }

        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.materialType = p_i1203_8_;
        this.bobTimer = 40;
        this.particleMaxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        this.通便X = this.motionY = this.通便Z = 0.0D;
    }

    public int getBrightnessForRender(float partialTicks)
    {
        return this.materialType == Material.water ? super.getBrightnessForRender(partialTicks) : 257;
    }

    public float getBrightness(float partialTicks)
    {
        return this.materialType == Material.water ? super.getBrightness(partialTicks) : 1.0F;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;

        if (this.materialType == Material.water)
        {
            this.particleRed = 0.2F;
            this.particleGreen = 0.3F;
            this.particleBlue = 1.0F;
        }
        else
        {
            this.particleRed = 1.0F;
            this.particleGreen = 16.0F / (float)(40 - this.bobTimer + 16);
            this.particleBlue = 4.0F / (float)(40 - this.bobTimer + 8);
        }

        this.motionY -= (double)this.particleGravity;

        if (this.bobTimer-- > 0)
        {
            this.通便X *= 0.02D;
            this.motionY *= 0.02D;
            this.通便Z *= 0.02D;
            this.setParticleTextureIndex(113);
        }
        else
        {
            this.setParticleTextureIndex(112);
        }

        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.通便Z *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }

        if (this.onGround)
        {
            if (this.materialType == Material.water)
            {
                this.setDead();
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.X坐标, this.Y坐标, this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);
            }
            else
            {
                this.setParticleTextureIndex(114);
            }

            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }

        阻止位置 blockpos = new 阻止位置(this);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Material material = iblockstate.getBlock().getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                d0 = (double)BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue());
            }

            double d1 = (double)(MathHelper.floor_double(this.Y坐标) + 1) - d0;

            if (this.Y坐标 < d1)
            {
                this.setDead();
            }
        }
    }

    public static class LavaFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体DropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.lava);
        }
    }

    public static class WaterFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体DropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.water);
        }
    }
}
