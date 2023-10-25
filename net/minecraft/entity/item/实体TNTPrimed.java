package net.minecraft.entity.item;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class 实体TNTPrimed extends 实体
{
    public int fuse;
    private 实体LivingBase tntPlacedBy;

    public 实体TNTPrimed(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
    }

    public 实体TNTPrimed(World worldIn, double x, double y, double z, 实体LivingBase igniter)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * Math.PI * 2.0D);
        this.通便X = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.通便Z = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.fuse = 80;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    protected void entityInit()
    {
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.通便Z *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0)
        {
            this.setDead();

            if (!this.worldObj.isRemote)
            {
                this.explode();
            }
        }
        else
        {
            this.handleWaterMovement();
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.X坐标, this.Y坐标 + 0.5D, this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    private void explode()
    {
        float f = 4.0F;
        this.worldObj.createExplosion(this, this.X坐标, this.Y坐标 + (double)(this.height / 16.0F), this.Z坐标, f, true);
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Fuse", (byte)this.fuse);
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.fuse = tagCompund.getByte("Fuse");
    }

    public 实体LivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }
}
