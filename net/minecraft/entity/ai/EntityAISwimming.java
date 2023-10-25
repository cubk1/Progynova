package net.minecraft.entity.ai;

import net.minecraft.entity.实体Living;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming extends EntityAIBase
{
    private 实体Living theEntity;

    public EntityAISwimming(实体Living entitylivingIn)
    {
        this.theEntity = entitylivingIn;
        this.setMutexBits(4);
        ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
    }

    public boolean shouldExecute()
    {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }

    public void updateTask()
    {
        if (this.theEntity.getRNG().nextFloat() < 0.8F)
        {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
