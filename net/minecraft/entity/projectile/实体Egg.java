package net.minecraft.entity.projectile;

import net.minecraft.entity.passive.实体Chicken;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体Egg extends 实体Throwable
{
    public 实体Egg(World worldIn)
    {
        super(worldIn);
    }

    public 实体Egg(World worldIn, 实体LivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public 实体Egg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        if (p_70184_1_.实体Hit != null)
        {
            p_70184_1_.实体Hit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0)
        {
            int i = 1;

            if (this.rand.nextInt(32) == 0)
            {
                i = 4;
            }

            for (int j = 0; j < i; ++j)
            {
                实体Chicken entitychicken = new 实体Chicken(this.worldObj);
                entitychicken.setGrowingAge(-24000);
                entitychicken.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, 0.0F);
                this.worldObj.spawnEntityInWorld(entitychicken);
            }
        }

        double d0 = 0.08D;

        for (int k = 0; k < 8; ++k)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.X坐标, this.Y坐标, this.Z坐标, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(Items.egg)});
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}
