package net.minecraft.entity.ai;

import net.minecraft.entity.实体Creature;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase
{
    private 实体Creature theEntity;

    public EntityAIRestrictSun(实体Creature creature)
    {
        this.theEntity = creature;
    }

    public boolean shouldExecute()
    {
        return this.theEntity.worldObj.isDaytime();
    }

    public void startExecuting()
    {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
    }

    public void resetTask()
    {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
    }
}
