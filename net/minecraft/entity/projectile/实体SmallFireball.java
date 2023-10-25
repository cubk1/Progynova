package net.minecraft.entity.projectile;

import net.minecraft.entity.实体Living;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体SmallFireball extends 实体Fireball
{
    public 实体SmallFireball(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
    }

    public 实体SmallFireball(World worldIn, 实体LivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    public 实体SmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObject.实体Hit != null)
            {
                boolean flag = movingObject.实体Hit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);

                if (flag)
                {
                    this.applyEnchantments(this.shootingEntity, movingObject.实体Hit);

                    if (!movingObject.实体Hit.isImmuneToFire())
                    {
                        movingObject.实体Hit.setFire(5);
                    }
                }
            }
            else
            {
                boolean flag1 = true;

                if (this.shootingEntity != null && this.shootingEntity instanceof 实体Living)
                {
                    flag1 = this.worldObj.getGameRules().getBoolean("mobGriefing");
                }

                if (flag1)
                {
                    阻止位置 blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);

                    if (this.worldObj.isAirBlock(blockpos))
                    {
                        this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
                    }
                }
            }

            this.setDead();
        }
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
