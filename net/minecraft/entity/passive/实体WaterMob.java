package net.minecraft.entity.passive;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体Living;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class 实体WaterMob extends 实体Living implements IAnimals
{
    public 实体WaterMob(World worldIn)
    {
        super(worldIn);
    }

    public boolean canBreatheUnderwater()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        return true;
    }

    public boolean isNotColliding()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    public int getTalkInterval()
    {
        return 120;
    }

    protected boolean canDespawn()
    {
        return true;
    }

    protected int getExperiencePoints(实体Player player)
    {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    public void onEntityUpdate()
    {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater())
        {
            --i;
            this.setAir(i);

            if (this.getAir() == -20)
            {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2.0F);
            }
        }
        else
        {
            this.setAir(300);
        }
    }

    public boolean isPushedByWater()
    {
        return false;
    }
}
