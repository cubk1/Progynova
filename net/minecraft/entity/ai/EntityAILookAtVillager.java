package net.minecraft.entity.ai;

import net.minecraft.entity.monster.实体IronGolem;
import net.minecraft.entity.passive.实体Villager;

public class EntityAILookAtVillager extends EntityAIBase
{
    private 实体IronGolem theGolem;
    private 实体Villager theVillager;
    private int lookTime;

    public EntityAILookAtVillager(实体IronGolem theGolemIn)
    {
        this.theGolem = theGolemIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        if (!this.theGolem.worldObj.isDaytime())
        {
            return false;
        }
        else if (this.theGolem.getRNG().nextInt(8000) != 0)
        {
            return false;
        }
        else
        {
            this.theVillager = (实体Villager)this.theGolem.worldObj.findNearestEntityWithinAABB(实体Villager.class, this.theGolem.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D), this.theGolem);
            return this.theVillager != null;
        }
    }

    public boolean continueExecuting()
    {
        return this.lookTime > 0;
    }

    public void startExecuting()
    {
        this.lookTime = 400;
        this.theGolem.setHoldingRose(true);
    }

    public void resetTask()
    {
        this.theGolem.setHoldingRose(false);
        this.theVillager = null;
    }

    public void updateTask()
    {
        this.theGolem.getLookHelper().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
        --this.lookTime;
    }
}
