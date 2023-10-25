package net.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.player.实体Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class 实体Hanging extends 实体
{
    private int tickCounter1;
    protected 阻止位置 hangingPosition;
    public EnumFacing facingDirection;

    public 实体Hanging(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }

    public 实体Hanging(World worldIn, 阻止位置 hangingPositionIn)
    {
        this(worldIn);
        this.hangingPosition = hangingPositionIn;
    }

    protected void entityInit()
    {
    }

    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        this.prevRotationYaw = this.旋转侧滑 = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.updateBoundingBox();
    }

    private void updateBoundingBox()
    {
        if (this.facingDirection != null)
        {
            double d0 = (double)this.hangingPosition.getX() + 0.5D;
            double d1 = (double)this.hangingPosition.getY() + 0.5D;
            double d2 = (double)this.hangingPosition.getZ() + 0.5D;
            double d3 = 0.46875D;
            double d4 = this.func_174858_a(this.getWidthPixels());
            double d5 = this.func_174858_a(this.getHeightPixels());
            d0 = d0 - (double)this.facingDirection.getFrontOffsetX() * 0.46875D;
            d2 = d2 - (double)this.facingDirection.getFrontOffsetZ() * 0.46875D;
            d1 = d1 + d5;
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            d0 = d0 + d4 * (double)enumfacing.getFrontOffsetX();
            d2 = d2 + d4 * (double)enumfacing.getFrontOffsetZ();
            this.X坐标 = d0;
            this.Y坐标 = d1;
            this.Z坐标 = d2;
            double d6 = (double)this.getWidthPixels();
            double d7 = (double)this.getHeightPixels();
            double d8 = (double)this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
            {
                d8 = 1.0D;
            }
            else
            {
                d6 = 1.0D;
            }

            d6 = d6 / 32.0D;
            d7 = d7 / 32.0D;
            d8 = d8 / 32.0D;
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
        }
    }

    private double func_174858_a(int p_174858_1_)
    {
        return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;

        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote)
        {
            this.tickCounter1 = 0;

            if (!this.isDead && !this.onValidSurface())
            {
                this.setDead();
                this.onBroken((实体)null);
            }
        }
    }

    public boolean onValidSurface()
    {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
        {
            return false;
        }
        else
        {
            int i = Math.max(1, this.getWidthPixels() / 16);
            int j = Math.max(1, this.getHeightPixels() / 16);
            阻止位置 blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();

            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    阻止位置 blockpos1 = blockpos.offset(enumfacing, k).up(l);
                    Block block = this.worldObj.getBlockState(blockpos1).getBlock();

                    if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(block))
                    {
                        return false;
                    }
                }
            }

            for (实体 实体 : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()))
            {
                if (实体 instanceof 实体Hanging)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean hitByEntity(实体 实体In)
    {
        return 实体In instanceof 实体Player ? this.attackEntityFrom(DamageSource.causePlayerDamage((实体Player) 实体In), 0.0F) : false;
    }

    public EnumFacing getHorizontalFacing()
    {
        return this.facingDirection;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                this.onBroken(source.getEntity());
            }

            return true;
        }
    }

    public void moveEntity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((实体)null);
        }
    }

    public void addVelocity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((实体)null);
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.getHangingPosition().getX());
        tagCompound.setInteger("TileY", this.getHangingPosition().getY());
        tagCompound.setInteger("TileZ", this.getHangingPosition().getZ());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.hangingPosition = new 阻止位置(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing enumfacing;

        if (tagCompund.hasKey("Direction", 99))
        {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.hangingPosition = this.hangingPosition.offset(enumfacing);
        }
        else if (tagCompund.hasKey("Facing", 99))
        {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
        }
        else
        {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }

        this.updateFacingWithBoundingBox(enumfacing);
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    public abstract void onBroken(实体 broken实体);

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    public void setPosition(double x, double y, double z)
    {
        this.X坐标 = x;
        this.Y坐标 = y;
        this.Z坐标 = z;
        阻止位置 blockpos = this.hangingPosition;
        this.hangingPosition = new 阻止位置(x, y, z);

        if (!this.hangingPosition.equals(blockpos))
        {
            this.updateBoundingBox();
            this.isAirBorne = true;
        }
    }

    public 阻止位置 getHangingPosition()
    {
        return this.hangingPosition;
    }
}
