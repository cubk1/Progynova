package net.minecraft.entity.projectile;

import net.minecraft.entity.monster.实体Blaze;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体Snowball extends 实体Throwable
{
    public 实体Snowball(World worldIn)
    {
        super(worldIn);
    }

    public 实体Snowball(World worldIn, 实体LivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public 实体Snowball(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        if (p_70184_1_.实体Hit != null)
        {
            int i = 0;

            if (p_70184_1_.实体Hit instanceof 实体Blaze)
            {
                i = 3;
            }

            p_70184_1_.实体Hit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
        }

        for (int j = 0; j < 8; ++j)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.X坐标, this.Y坐标, this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}
