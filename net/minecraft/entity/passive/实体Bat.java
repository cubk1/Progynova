package net.minecraft.entity.passive;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体Bat extends 实体AmbientCreature
{
    private 阻止位置 spawnPosition;

    public 实体Bat(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(true);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    protected float getSoundVolume()
    {
        return 0.1F;
    }

    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    protected String getLivingSound()
    {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    protected String getHurtSound()
    {
        return "mob.bat.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.bat.death";
    }

    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(实体 实体In)
    {
    }

    protected void collideWithNearbyEntities()
    {
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
    }

    public boolean getIsBatHanging()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setIsBatHanging(boolean isHanging)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (isHanging)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.getIsBatHanging())
        {
            this.通便X = this.motionY = this.通便Z = 0.0D;
            this.Y坐标 = (double)MathHelper.floor_double(this.Y坐标) + 1.0D - (double)this.height;
        }
        else
        {
            this.motionY *= 0.6000000238418579D;
        }
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        阻止位置 blockpos = new 阻止位置(this);
        阻止位置 blockpos1 = blockpos.up();

        if (this.getIsBatHanging())
        {
            if (!this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube())
            {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity((实体Player)null, 1015, blockpos, 0);
            }
            else
            {
                if (this.rand.nextInt(200) == 0)
                {
                    this.rotationYawHead = (float)this.rand.nextInt(360);
                }

                if (this.worldObj.getClosestPlayerToEntity(this, 4.0D) != null)
                {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity((实体Player)null, 1015, blockpos, 0);
                }
            }
        }
        else
        {
            if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
            {
                this.spawnPosition = null;
            }

            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double)((int)this.X坐标), (double)((int)this.Y坐标), (double)((int)this.Z坐标)) < 4.0D)
            {
                this.spawnPosition = new 阻止位置((int)this.X坐标 + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.Y坐标 + this.rand.nextInt(6) - 2, (int)this.Z坐标 + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double d0 = (double)this.spawnPosition.getX() + 0.5D - this.X坐标;
            double d1 = (double)this.spawnPosition.getY() + 0.1D - this.Y坐标;
            double d2 = (double)this.spawnPosition.getZ() + 0.5D - this.Z坐标;
            this.通便X += (Math.signum(d0) * 0.5D - this.通便X) * 0.10000000149011612D;
            this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
            this.通便Z += (Math.signum(d2) * 0.5D - this.通便Z) * 0.10000000149011612D;
            float f = (float)(MathHelper.atan2(this.通便Z, this.通便X) * 180.0D / Math.PI) - 90.0F;
            float f1 = MathHelper.wrapAngleTo180_float(f - this.旋转侧滑);
            this.moveForward = 0.5F;
            this.旋转侧滑 += f1;

            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube())
            {
                this.setIsBatHanging(true);
            }
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, 阻止位置 pos)
    {
    }

    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isRemote && this.getIsBatHanging())
            {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.dataWatcher.updateObject(16, Byte.valueOf(tagCompund.getByte("BatFlags")));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }

    public boolean getCanSpawnHere()
    {
        阻止位置 blockpos = new 阻止位置(this.X坐标, this.getEntityBoundingBox().minY, this.Z坐标);

        if (blockpos.getY() >= this.worldObj.getSeaLevel())
        {
            return false;
        }
        else
        {
            int i = this.worldObj.getLightFromNeighbors(blockpos);
            int j = 4;

            if (this.isDateAroundHalloween(this.worldObj.getCurrentDate()))
            {
                j = 7;
            }
            else if (this.rand.nextBoolean())
            {
                return false;
            }

            return i > this.rand.nextInt(j) ? false : super.getCanSpawnHere();
        }
    }

    private boolean isDateAroundHalloween(Calendar p_175569_1_)
    {
        return p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20 || p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3;
    }

    public float getEyeHeight()
    {
        return this.height / 2.0F;
    }
}
