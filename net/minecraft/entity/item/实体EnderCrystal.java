package net.minecraft.entity.item;

import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class 实体EnderCrystal extends 实体
{
    public int innerRotation;
    public int health;

    public 实体EnderCrystal(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.health = 5;
        this.innerRotation = this.rand.nextInt(100000);
    }

    public 实体EnderCrystal(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(8, Integer.valueOf(this.health));
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        ++this.innerRotation;
        this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
        int i = MathHelper.floor_double(this.X坐标);
        int j = MathHelper.floor_double(this.Y坐标);
        int k = MathHelper.floor_double(this.Z坐标);

        if (this.worldObj.provider instanceof WorldProviderEnd && this.worldObj.getBlockState(new 阻止位置(i, j, k)).getBlock() != Blocks.fire)
        {
            this.worldObj.setBlockState(new 阻止位置(i, j, k), Blocks.fire.getDefaultState());
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    public boolean canBeCollidedWith()
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
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.health = 0;

                if (this.health <= 0)
                {
                    this.setDead();

                    if (!this.worldObj.isRemote)
                    {
                        this.worldObj.createExplosion((实体)null, this.X坐标, this.Y坐标, this.Z坐标, 6.0F, true);
                    }
                }
            }

            return true;
        }
    }
}
