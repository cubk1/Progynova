package net.minecraft.entity.ai;

import net.minecraft.entity.passive.实体Tameable;
import net.minecraft.entity.实体LivingBase;

public class EntityAISit extends EntityAIBase
{
    private 实体Tameable theEntity;
    private boolean isSitting;

    public EntityAISit(实体Tameable entityIn)
    {
        this.theEntity = entityIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        if (!this.theEntity.isTamed())
        {
            return false;
        }
        else if (this.theEntity.isInWater())
        {
            return false;
        }
        else if (!this.theEntity.onGround)
        {
            return false;
        }
        else
        {
            实体LivingBase entitylivingbase = this.theEntity.getOwner();
            return entitylivingbase == null ? true : (this.theEntity.getDistanceSqToEntity(entitylivingbase) < 144.0D && entitylivingbase.getAITarget() != null ? false : this.isSitting);
        }
    }

    public void startExecuting()
    {
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.setSitting(true);
    }

    public void resetTask()
    {
        this.theEntity.setSitting(false);
    }

    public void setSitting(boolean sitting)
    {
        this.isSitting = sitting;
    }
}
