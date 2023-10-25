package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体RainFX extends 实体FX
{
    protected 实体RainFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.通便X *= 0.30000001192092896D;
        this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        this.通便Z *= 0.30000001192092896D;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setParticleTextureIndex(19 + this.rand.nextInt(4));
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
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

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }

        if (this.onGround)
        {
            if (Math.random() < 0.5D)
            {
                this.setDead();
            }

            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }

        阻止位置 blockpos = new 阻止位置(this);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
        Material material = iblockstate.getBlock().getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                d0 = (double)(1.0F - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue()));
            }
            else
            {
                d0 = block.getBlockBoundsMaxY();
            }

            double d1 = (double)MathHelper.floor_double(this.Y坐标) + d0;

            if (this.Y坐标 < d1)
            {
                this.setDead();
            }
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体RainFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
