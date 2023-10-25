package net.minecraft.entity.item;

import net.minecraft.entity.实体;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体EnderEye extends 实体
{
    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;

    public 实体EnderEye(World worldIn)
    {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    public 实体EnderEye(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.despawnTimer = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
    }

    public void moveTowards(阻止位置 p_180465_1_)
    {
        double d0 = (double)p_180465_1_.getX();
        int i = p_180465_1_.getY();
        double d1 = (double)p_180465_1_.getZ();
        double d2 = d0 - this.X坐标;
        double d3 = d1 - this.Z坐标;
        float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);

        if (f > 12.0F)
        {
            this.targetX = this.X坐标 + d2 / (double)f * 12.0D;
            this.targetZ = this.Z坐标 + d3 / (double)f * 12.0D;
            this.targetY = this.Y坐标 + 8.0D;
        }
        else
        {
            this.targetX = d0;
            this.targetY = (double)i;
            this.targetZ = d1;
        }

        this.despawnTimer = 0;
        this.shatterOrDrop = this.rand.nextInt(5) > 0;
    }

    public void setVelocity(double x, double y, double z)
    {
        this.通便X = x;
        this.motionY = y;
        this.通便Z = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.旋转侧滑 = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * 180.0D / Math.PI);
        }
    }

    public void onUpdate()
    {
        this.lastTickPosX = this.X坐标;
        this.lastTickPosY = this.Y坐标;
        this.lastTickPosZ = this.Z坐标;
        super.onUpdate();
        this.X坐标 += this.通便X;
        this.Y坐标 += this.motionY;
        this.Z坐标 += this.通便Z;
        float f = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
        this.旋转侧滑 = (float)(MathHelper.atan2(this.通便X, this.通便Z) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.旋转侧滑 - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.旋转侧滑 - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.旋转侧滑 = this.prevRotationYaw + (this.旋转侧滑 - this.prevRotationYaw) * 0.2F;

        if (!this.worldObj.isRemote)
        {
            double d0 = this.targetX - this.X坐标;
            double d1 = this.targetZ - this.Z坐标;
            float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
            float f2 = (float)MathHelper.atan2(d1, d0);
            double d2 = (double)f + (double)(f1 - f) * 0.0025D;

            if (f1 < 1.0F)
            {
                d2 *= 0.8D;
                this.motionY *= 0.8D;
            }

            this.通便X = Math.cos((double)f2) * d2;
            this.通便Z = Math.sin((double)f2) * d2;

            if (this.Y坐标 < this.targetY)
            {
                this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
            }
            else
            {
                this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
            }
        }

        float f3 = 0.25F;

        if (this.isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.X坐标 - this.通便X * (double)f3, this.Y坐标 - this.motionY * (double)f3, this.Z坐标 - this.通便Z * (double)f3, this.通便X, this.motionY, this.通便Z, new int[0]);
            }
        }
        else
        {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.X坐标 - this.通便X * (double)f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.Y坐标 - this.motionY * (double)f3 - 0.5D, this.Z坐标 - this.通便Z * (double)f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.通便X, this.motionY, this.通便Z, new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
            ++this.despawnTimer;

            if (this.despawnTimer > 80 && !this.worldObj.isRemote)
            {
                this.setDead();

                if (this.shatterOrDrop)
                {
                    this.worldObj.spawnEntityInWorld(new 实体Item(this.worldObj, this.X坐标, this.Y坐标, this.Z坐标, new ItemStack(Items.ender_eye)));
                }
                else
                {
                    this.worldObj.playAuxSFX(2003, new 阻止位置(this), 0);
                }
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    public float getBrightness(float partialTicks)
    {
        return 1.0F;
    }

    public int getBrightnessForRender(float partialTicks)
    {
        return 15728880;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
}
