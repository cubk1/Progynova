package net.minecraft.entity;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class 实体LeashKnot extends 实体Hanging
{
    public 实体LeashKnot(World worldIn)
    {
        super(worldIn);
    }

    public 实体LeashKnot(World worldIn, 阻止位置 hangingPositionIn)
    {
        super(worldIn, hangingPositionIn);
        this.setPosition((double)hangingPositionIn.getX() + 0.5D, (double)hangingPositionIn.getY() + 0.5D, (double)hangingPositionIn.getZ() + 0.5D);
        float f = 0.125F;
        float f1 = 0.1875F;
        float f2 = 0.25F;
        this.setEntityBoundingBox(new AxisAlignedBB(this.X坐标 - 0.1875D, this.Y坐标 - 0.25D + 0.125D, this.Z坐标 - 0.1875D, this.X坐标 + 0.1875D, this.Y坐标 + 0.25D + 0.125D, this.Z坐标 + 0.1875D));
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
    }

    public int getWidthPixels()
    {
        return 9;
    }

    public int getHeightPixels()
    {
        return 9;
    }

    public float getEyeHeight()
    {
        return -0.0625F;
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 1024.0D;
    }

    public void onBroken(实体 broken实体)
    {
    }

    public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    public boolean interactFirst(实体Player playerIn)
    {
        ItemStack itemstack = playerIn.getHeldItem();
        boolean flag = false;

        if (itemstack != null && itemstack.getItem() == Items.lead && !this.worldObj.isRemote)
        {
            double d0 = 7.0D;

            for (实体Living entityliving : this.worldObj.getEntitiesWithinAABB(实体Living.class, new AxisAlignedBB(this.X坐标 - d0, this.Y坐标 - d0, this.Z坐标 - d0, this.X坐标 + d0, this.Y坐标 + d0, this.Z坐标 + d0)))
            {
                if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn)
                {
                    entityliving.setLeashedToEntity(this, true);
                    flag = true;
                }
            }
        }

        if (!this.worldObj.isRemote && !flag)
        {
            this.setDead();

            if (playerIn.capabilities.isCreativeMode)
            {
                double d1 = 7.0D;

                for (实体Living entityliving1 : this.worldObj.getEntitiesWithinAABB(实体Living.class, new AxisAlignedBB(this.X坐标 - d1, this.Y坐标 - d1, this.Z坐标 - d1, this.X坐标 + d1, this.Y坐标 + d1, this.Z坐标 + d1)))
                {
                    if (entityliving1.getLeashed() && entityliving1.getLeashedToEntity() == this)
                    {
                        entityliving1.clearLeashed(true, false);
                    }
                }
            }
        }

        return true;
    }

    public boolean onValidSurface()
    {
        return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }

    public static 实体LeashKnot createKnot(World worldIn, 阻止位置 fence)
    {
        实体LeashKnot entityleashknot = new 实体LeashKnot(worldIn, fence);
        entityleashknot.forceSpawn = true;
        worldIn.spawnEntityInWorld(entityleashknot);
        return entityleashknot;
    }

    public static 实体LeashKnot getKnotForPosition(World worldIn, 阻止位置 pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (实体LeashKnot entityleashknot : worldIn.getEntitiesWithinAABB(实体LeashKnot.class, new AxisAlignedBB((double)i - 1.0D, (double)j - 1.0D, (double)k - 1.0D, (double)i + 1.0D, (double)j + 1.0D, (double)k + 1.0D)))
        {
            if (entityleashknot.getHangingPosition().equals(pos))
            {
                return entityleashknot;
            }
        }

        return null;
    }
}
