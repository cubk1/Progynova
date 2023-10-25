package net.minecraft.entity.ai;

import net.minecraft.entity.实体Living;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase
{
    World theWorld;
    实体Living theEntity;
    实体LivingBase theVictim;
    int attackCountdown;

    public EntityAIOcelotAttack(实体Living theEntityIn)
    {
        this.theEntity = theEntityIn;
        this.theWorld = theEntityIn.worldObj;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        实体LivingBase entitylivingbase = this.theEntity.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.theVictim = entitylivingbase;
            return true;
        }
    }

    public boolean continueExecuting()
    {
        return !this.theVictim.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity(this.theVictim) > 225.0D ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute());
    }

    public void resetTask()
    {
        this.theVictim = null;
        this.theEntity.getNavigator().clearPathEntity();
    }

    public void updateTask()
    {
        this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0F, 30.0F);
        double d0 = (double)(this.theEntity.width * 2.0F * this.theEntity.width * 2.0F);
        double d1 = this.theEntity.getDistanceSq(this.theVictim.X坐标, this.theVictim.getEntityBoundingBox().minY, this.theVictim.Z坐标);
        double d2 = 0.8D;

        if (d1 > d0 && d1 < 16.0D)
        {
            d2 = 1.33D;
        }
        else if (d1 < 225.0D)
        {
            d2 = 0.6D;
        }

        this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, d2);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);

        if (d1 <= d0)
        {
            if (this.attackCountdown <= 0)
            {
                this.attackCountdown = 20;
                this.theEntity.attackEntityAsMob(this.theVictim);
            }
        }
    }
}
