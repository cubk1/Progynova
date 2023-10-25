package net.minecraft.entity.item;

import net.minecraft.entity.实体;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体FireworkRocket extends 实体
{
    private int fireworkAge;
    private int lifetime;

    public 实体FireworkRocket(World worldIn)
    {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObjectByDataType(8, 5);
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 4096.0D;
    }

    public 实体FireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem)
    {
        super(worldIn);
        this.fireworkAge = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        int i = 1;

        if (givenItem != null && givenItem.hasTagCompound())
        {
            this.dataWatcher.updateObject(8, givenItem);
            NBTTagCompound nbttagcompound = givenItem.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");

            if (nbttagcompound1 != null)
            {
                i += nbttagcompound1.getByte("Flight");
            }
        }

        this.通便X = this.rand.nextGaussian() * 0.001D;
        this.通便Z = this.rand.nextGaussian() * 0.001D;
        this.motionY = 0.05D;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
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
        this.通便X *= 1.15D;
        this.通便Z *= 1.15D;
        this.motionY += 0.04D;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
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

        if (this.fireworkAge == 0 && !this.isSilent())
        {
            this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
        }

        ++this.fireworkAge;

        if (this.worldObj.isRemote && this.fireworkAge % 2 < 2)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.X坐标, this.Y坐标 - 0.3D, this.Z坐标, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
        }

        if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime)
        {
            this.worldObj.setEntityState(this, (byte)17);
            this.setDead();
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && this.worldObj.isRemote)
        {
            ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound nbttagcompound = null;

            if (itemstack != null && itemstack.hasTagCompound())
            {
                nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
            }

            this.worldObj.makeFireworks(this.X坐标, this.Y坐标, this.Z坐标, this.通便X, this.motionY, this.通便Z, nbttagcompound);
        }

        super.handleStatusUpdate(id);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("Life", this.fireworkAge);
        tagCompound.setInteger("LifeTime", this.lifetime);
        ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);

        if (itemstack != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            itemstack.writeToNBT(nbttagcompound);
            tagCompound.setTag("FireworksItem", nbttagcompound);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.fireworkAge = tagCompund.getInteger("Life");
        this.lifetime = tagCompund.getInteger("LifeTime");
        NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                this.dataWatcher.updateObject(8, itemstack);
            }
        }
    }

    public float getBrightness(float partialTicks)
    {
        return super.getBrightness(partialTicks);
    }

    public int getBrightnessForRender(float partialTicks)
    {
        return super.getBrightnessForRender(partialTicks);
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
}
