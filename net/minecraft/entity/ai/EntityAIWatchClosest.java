package net.minecraft.entity.ai;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;

public class EntityAIWatchClosest extends EntityAIBase
{
    protected 实体Living theWatcher;
    protected 实体 closest实体;
    protected float maxDistanceForPlayer;
    private int lookTime;
    private float chance;
    protected Class <? extends 实体> watchedClass;

    public EntityAIWatchClosest(实体Living entitylivingIn, Class <? extends 实体> watchTargetClass, float maxDistance)
    {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = 0.02F;
        this.setMutexBits(2);
    }

    public EntityAIWatchClosest(实体Living entitylivingIn, Class <? extends 实体> watchTargetClass, float maxDistance, float chanceIn)
    {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(2);
    }

    public boolean shouldExecute()
    {
        if (this.theWatcher.getRNG().nextFloat() >= this.chance)
        {
            return false;
        }
        else
        {
            if (this.theWatcher.getAttackTarget() != null)
            {
                this.closest实体 = this.theWatcher.getAttackTarget();
            }

            if (this.watchedClass == 实体Player.class)
            {
                this.closest实体 = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, (double)this.maxDistanceForPlayer);
            }
            else
            {
                this.closest实体 = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand((double)this.maxDistanceForPlayer, 3.0D, (double)this.maxDistanceForPlayer), this.theWatcher);
            }

            return this.closest实体 != null;
        }
    }

    public boolean continueExecuting()
    {
        return !this.closest实体.isEntityAlive() ? false : (this.theWatcher.getDistanceSqToEntity(this.closest实体) > (double)(this.maxDistanceForPlayer * this.maxDistanceForPlayer) ? false : this.lookTime > 0);
    }

    public void startExecuting()
    {
        this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
    }

    public void resetTask()
    {
        this.closest实体 = null;
    }

    public void updateTask()
    {
        this.theWatcher.getLookHelper().setLookPosition(this.closest实体.X坐标, this.closest实体.Y坐标 + (double)this.closest实体.getEyeHeight(), this.closest实体.Z坐标, 10.0F, (float)this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }
}
