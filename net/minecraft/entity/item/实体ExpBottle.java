package net.minecraft.entity.item;

import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.projectile.实体Throwable;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体ExpBottle extends 实体Throwable
{
    public 实体ExpBottle(World worldIn)
    {
        super(worldIn);
    }

    public 实体ExpBottle(World worldIn, 实体LivingBase p_i1786_2_)
    {
        super(worldIn, p_i1786_2_);
    }

    public 实体ExpBottle(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected float getGravityVelocity()
    {
        return 0.07F;
    }

    protected float getVelocity()
    {
        return 0.7F;
    }

    protected float getInaccuracy()
    {
        return -20.0F;
    }

    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        if (!this.worldObj.isRemote)
        {
            this.worldObj.playAuxSFX(2002, new 阻止位置(this), 0);
            int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);

            while (i > 0)
            {
                int j = 实体XPOrb.getXPSplit(i);
                i -= j;
                this.worldObj.spawnEntityInWorld(new 实体XPOrb(this.worldObj, this.X坐标, this.Y坐标, this.Z坐标, j));
            }

            this.setDead();
        }
    }
}
